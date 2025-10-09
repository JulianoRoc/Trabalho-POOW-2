package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.Funcionario;
import br.csi.sistema_biblioteca.service.FuncionarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @PostMapping
    public ResponseEntity<Funcionario> salvar(@RequestBody @Valid Funcionario funcionario,
                                              UriComponentsBuilder uriBuilder) {
        Funcionario funcionarioSalvo = funcionarioService.salvar(funcionario);
        var uri = uriBuilder.path("/funcionarios/{id}").buildAndExpand(funcionarioSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(funcionarioSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Funcionario>> listarAtivos() {
        List<Funcionario> funcionarios = funcionarioService.listarAtivos();
        return ResponseEntity.ok(funcionarios);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@PathVariable Long id,
                                                 @RequestBody @Valid Funcionario funcionario) {
        Funcionario funcionarioAtualizado = funcionarioService.atualizar(id, funcionario);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@PathVariable Long id) {
        funcionarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@PathVariable Long id) {
        funcionarioService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/login")
    public ResponseEntity<Funcionario> login(@RequestBody LoginRequest request) {
        Funcionario funcionario = funcionarioService.login(request.email(), request.senha());
        return ResponseEntity.ok(funcionario);
    }

    public record LoginRequest(String email, String senha) {}
}