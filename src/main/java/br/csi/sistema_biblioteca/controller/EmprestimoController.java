package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.*;
import br.csi.sistema_biblioteca.service.EmprestimoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/emprestimos")
@RequiredArgsConstructor
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @PostMapping
    public ResponseEntity<Emprestimo> realizarEmprestimo(
            @RequestBody @Valid EmprestimoRequest request,
            UriComponentsBuilder uriBuilder) {

        Emprestimo emprestimo = emprestimoService.realizarEmprestimo(
                request.clienteId(),
                request.livroId(),
                request.funcionarioId()
        );

        var uri = uriBuilder.path("/emprestimos/{id}").buildAndExpand(emprestimo.getId()).toUri();
        return ResponseEntity.created(uri).body(emprestimo);
    }

    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Emprestimo> registrarDevolucao(
            @PathVariable Long id,
            @RequestBody @Valid DevolucaoRequest request) {

        Emprestimo emprestimo = emprestimoService.registrarDevolucao(id, request.funcionarioId());
        return ResponseEntity.ok(emprestimo);
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Emprestimo>> listarEmprestimosAtivos() {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAtivos();
        return ResponseEntity.ok(emprestimos);
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Emprestimo>> listarHistoricoCliente(@PathVariable Long clienteId) {
        List<Emprestimo> historico = emprestimoService.listarHistoricoCliente(clienteId);
        return ResponseEntity.ok(historico);
    }

    public record EmprestimoRequest(
            @NotNull Long clienteId,
            @NotNull Long livroId,
            @NotNull Long funcionarioId
    ) {}

    public record DevolucaoRequest(@NotNull Long funcionarioId) {}
}
