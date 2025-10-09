package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.*;
import br.csi.sistema_biblioteca.repository.*;
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
        Emprestimo emprestimo = new Emprestimo();
        emprestimo.setCliente(cliente);
        emprestimo.setLivro(livro);
        emprestimo.setFuncionario(funcionario);
        emprestimo.setDataEmprestimo(LocalDateTime.now());
        emprestimo.setDataDevolucaoPrevista(LocalDateTime.now().plusDays(14)); // 2 semanas

        // Marcar livro como indisponível
        livro.setDisponivel(false);
        livroRepository.save(livro);

        return emprestimoRepository.save(emprestimo);
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

        return emprestimoRepository.save(emprestimo);
    }

    public List<Emprestimo> listarEmprestimosAtivos() {
        return emprestimoRepository.findByDataDevolucaoIsNull();
    }

    public List<Emprestimo> listarHistoricoCliente(Long clienteId) {
        return emprestimoRepository.findByClienteId(clienteId);
    }
}
