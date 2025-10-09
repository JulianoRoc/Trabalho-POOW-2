package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.*;
import br.csi.sistema_biblioteca.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;

    public Funcionario salvar(Funcionario funcionario) {
        // Verificar se email já existe
        Optional<Funcionario> funcionarioExistente = funcionarioRepository.findByEmail(funcionario.getEmail());
        if (funcionarioExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado");
        }
        return funcionarioRepository.save(funcionario);
    }

    public List<Funcionario> listarTodos() {
        return funcionarioRepository.findAll();
    }

    public List<Funcionario> listarAtivos() {
        return funcionarioRepository.findAll().stream()
                .filter(Funcionario::getAtivo)
                .toList();
    }

    public Funcionario buscarPorId(Long id) {
        return funcionarioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Funcionário não encontrado"));
    }

    public Funcionario atualizar(Long id, Funcionario funcionarioAtualizado) {
        Funcionario funcionario = buscarPorId(id);

        // Verificar se email já existe em outro funcionário
        Optional<Funcionario> funcionarioComEmail = funcionarioRepository.findByEmail(funcionarioAtualizado.getEmail());
        if (funcionarioComEmail.isPresent() && !funcionarioComEmail.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email já cadastrado em outro funcionário");
        }

        funcionario.setNome(funcionarioAtualizado.getNome());
        funcionario.setEmail(funcionarioAtualizado.getEmail());
        funcionario.setSenha(funcionarioAtualizado.getSenha());
        funcionario.setAtivo(funcionarioAtualizado.getAtivo());

        return funcionarioRepository.save(funcionario);
    }

    public void desativar(Long id) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(false);
        funcionarioRepository.save(funcionario);
    }

    public void ativar(Long id) {
        Funcionario funcionario = buscarPorId(id);
        funcionario.setAtivo(true);
        funcionarioRepository.save(funcionario);
    }

    public Funcionario login(String email, String senha) {
        return funcionarioRepository.findByEmailAndSenha(email, senha)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciais inválidas"));
    }
}