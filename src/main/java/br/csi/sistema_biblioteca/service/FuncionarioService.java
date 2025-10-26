package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.funcionario.Funcionario;
import br.csi.sistema_biblioteca.model.funcionario.FuncionarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public Funcionario salvar(Funcionario funcionario) {
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findByEmail(funcionario.getEmail());
        if (funcionarioExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }

        return this.funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return this.funcionarioRepository.findAll();
    }

    public List<Funcionario> listarAtivos() {
        return this.funcionarioRepository.findByAtivoTrue();
    }

    public Funcionario buscarPorId(Long id) {
        return this.funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }

    public Funcionario buscarPorUuid(UUID uuid) {
        return this.funcionarioRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }

    public void excluir(Long id) {
        Funcionario funcionario = buscarPorId(id);
        this.funcionarioRepository.delete(funcionario);
    }

    public void excluirPorUuid(UUID uuid) {
        Funcionario funcionario = buscarPorUuid(uuid);
        this.funcionarioRepository.delete(funcionario);
    }

    public Funcionario atualizar(Long id, Funcionario funcionario) {
        Funcionario f = this.funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        // Verificar se email já existe em outro funcionário
        Optional<Funcionario> funcionarioComEmail = funcionarioRepository.findByEmail(funcionario.getEmail());
        if (funcionarioComEmail.isPresent() && !funcionarioComEmail.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado em outro funcionário");
        }

        f.setNome(funcionario.getNome());
        f.setEmail(funcionario.getEmail());
        if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
            f.setSenha(funcionario.getSenha()); // sem codificação
        }
        f.setAtivo(funcionario.getAtivo());

        return this.funcionarioRepository.save(f);
    }

    public Funcionario atualizarPorUuid(Funcionario funcionario) {
        Funcionario f = this.funcionarioRepository.findByUuid(funcionario.getUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));

        // Verificar se email já existe em outro funcionário
        Optional<Funcionario> funcionarioComEmail = funcionarioRepository.findByEmail(funcionario.getEmail());
        if (funcionarioComEmail.isPresent() && !funcionarioComEmail.get().getUuid().equals(funcionario.getUuid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado em outro funcionário");
        }

        f.setNome(funcionario.getNome());
        f.setEmail(funcionario.getEmail());
        if (funcionario.getSenha() != null && !funcionario.getSenha().isEmpty()) {
            f.setSenha(funcionario.getSenha()); // sem codificação
        }
        f.setAtivo(funcionario.getAtivo());

        return this.funcionarioRepository.save(f);
    }

    public void desativar(Long id) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(false);
        this.funcionarioRepository.save(funcionario);
    }

    public void desativarPorUuid(UUID uuid) {
        Funcionario funcionario = buscarPorUuid(uuid);
        funcionario.setAtivo(false);
        this.funcionarioRepository.save(funcionario);
    }

    public void ativar(Long id) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(true);
        this.funcionarioRepository.save(funcionario);
    }

    public void ativarPorUuid(UUID uuid) {
        Funcionario funcionario = buscarPorUuid(uuid);
        funcionario.setAtivo(true);
        this.funcionarioRepository.save(funcionario);
    }
}
