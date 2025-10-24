package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.cliente.Cliente;
import br.csi.sistema_biblioteca.model.cliente.ClienteRepository;
import br.csi.sistema_biblioteca.model.emprestimo.Emprestimo;
import br.csi.sistema_biblioteca.model.emprestimo.EmprestimoRepository;
import br.csi.sistema_biblioteca.model.funcionario.Funcionario;
import br.csi.sistema_biblioteca.model.funcionario.FuncionarioRepository;
import br.csi.sistema_biblioteca.model.livro_categoria.Livro;
import br.csi.sistema_biblioteca.model.livro_categoria.LivroRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final ClienteRepository clienteRepository;
    private final LivroRepository livroRepository;
    private final FuncionarioRepository funcionarioRepository;

    @Transactional
    public Emprestimo realizarEmprestimo(Long clienteId, Long livroId, Long funcionarioId) {
        // Verificar se cliente existe
        Cliente cliente = clienteRepository.findById(clienteId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Verificar se livro existe e está disponível
        Livro livro = livroRepository.findByIdAndDisponivelTrue(livroId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro não disponível para empréstimo"));

        // Verificar se funcionário existe e está ativo
        Funcionario funcionario = funcionarioRepository.findByIdAndAtivoTrue(funcionarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionário não está ativo"));

        // Verificar se cliente não tem muitos empréstimos ativos (máximo 3)
        int emprestimosAtivos = emprestimoRepository.countEmprestimosAtivosPorCliente(clienteId);
        if (emprestimosAtivos >= 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente atingiu o limite de empréstimos ativos");
        }

        // Verificar se livro já está emprestado
        emprestimoRepository.findEmprestimoAtivoPorLivro(livroId)
                .ifPresent(e -> {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro já está emprestado");
                });

        // Criar empréstimo
        Emprestimo emprestimo = new Emprestimo(cliente, livro, funcionario);

        // Marcar livro como indisponível
        livro.setDisponivel(false);
        livroRepository.save(livro);

        return this.emprestimoRepository.save(emprestimo);
    }

    @Transactional
    public Emprestimo registrarDevolucao(Long emprestimoId, Long funcionarioId) {
        Emprestimo emprestimo = emprestimoRepository.findById(emprestimoId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        if (emprestimo.getDataDevolucao() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Empréstimo já foi devolvido");
        }

        // Verificar se funcionário está ativo
        Funcionario funcionario = funcionarioRepository.findByIdAndAtivoTrue(funcionarioId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Funcionário não está ativo"));

        // Registrar devolução
        emprestimo.setDataDevolucao(LocalDateTime.now());
        emprestimo.setFuncionario(funcionario);

        // Marcar livro como disponível
        Livro livro = emprestimo.getLivro();
        livro.setDisponivel(true);
        livroRepository.save(livro);

        return this.emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> listarTodos() {
        return this.emprestimoRepository.findAll();
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return this.emprestimoRepository.findByDataDevolucaoIsNull();
    }

    public List<Emprestimo> listarEmprestimosAtrasados() {
        return this.emprestimoRepository.findEmprestimosAtrasados();
    }

    public List<Emprestimo> listarHistoricoCliente(Long clienteId) {
        return this.emprestimoRepository.findByClienteId(clienteId);
    }

    public List<Emprestimo> listarPorFuncionario(Long funcionarioId) {
        return this.emprestimoRepository.findByFuncionarioId(funcionarioId);
    }

    public Emprestimo buscarPorId(Long id) {
        return this.emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));
    }

    public void excluir(Long id) {
        Emprestimo emprestimo = buscarPorId(id);
        this.emprestimoRepository.delete(emprestimo);
    }

    /*
        Para atualizar uma entidade do banco é necessário pegar a referência desta
        entidade e atualizar com os dados que vieram por parametro.
        O save(...) detecta que esse RECURSO já existe no banco de dados pela busca por id
        assim ao executar o save com id ele faz um UPDATE
    */
    public Emprestimo atualizar(Long id, Emprestimo emprestimo) {
        Emprestimo e = this.emprestimoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empréstimo não encontrado"));

        e.setDataEmprestimo(emprestimo.getDataEmprestimo());
        e.setDataDevolucao(emprestimo.getDataDevolucao());
        e.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
        e.setCliente(emprestimo.getCliente());
        e.setLivro(emprestimo.getLivro());
        e.setFuncionario(emprestimo.getFuncionario());

        return this.emprestimoRepository.save(e);
    }
}
