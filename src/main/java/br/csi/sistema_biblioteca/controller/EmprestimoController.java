package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.emprestimo.Emprestimo;
import br.csi.sistema_biblioteca.service.EmprestimoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Empréstimos", description = "Operações de empréstimo e devolução de livros")
public class EmprestimoController {

    private final EmprestimoService emprestimoService;

    @Operation(summary = "Realizar empréstimo", description = "Registra um novo empréstimo de livro")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Empréstimo realizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos ou regras de negócio violadas"),
            @ApiResponse(responseCode = "404", description = "Cliente, livro ou funcionário não encontrado")
    })
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

    @Operation(summary = "Registrar devolução", description = "Registra a devolução de um livro emprestado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Devolução registrada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Empréstimo já foi devolvido"),
            @ApiResponse(responseCode = "404", description = "Empréstimo ou funcionário não encontrado")
    })
    @PutMapping("/{id}/devolucao")
    public ResponseEntity<Emprestimo> registrarDevolucao(
            @PathVariable Long id,
            @RequestBody @Valid DevolucaoRequest request) {

        Emprestimo emprestimo = emprestimoService.registrarDevolucao(id, request.funcionarioId());
        return ResponseEntity.ok(emprestimo);
    }

    @Operation(summary = "Listar empréstimos ativos", description = "Retorna todos os empréstimos que ainda não foram devolvidos")
    @GetMapping("/ativos")
    public ResponseEntity<List<Emprestimo>> listarEmprestimosAtivos() {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAtivos();
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(summary = "Listar empréstimos atrasados", description = "Retorna todos os empréstimos em atraso")
    @GetMapping("/atrasados")
    public ResponseEntity<List<Emprestimo>> listarEmprestimosAtrasados() {
        List<Emprestimo> emprestimos = emprestimoService.listarEmprestimosAtrasados();
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(summary = "Listar histórico do cliente", description = "Retorna todo o histórico de empréstimos de um cliente")
    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<List<Emprestimo>> listarHistoricoCliente(@PathVariable Long clienteId) {
        List<Emprestimo> historico = emprestimoService.listarHistoricoCliente(clienteId);
        return ResponseEntity.ok(historico);
    }

    @Operation(summary = "Listar empréstimos por funcionário", description = "Retorna todos os empréstimos registrados por um funcionário")
    @GetMapping("/funcionario/{funcionarioId}")
    public ResponseEntity<List<Emprestimo>> listarPorFuncionario(@PathVariable Long funcionarioId) {
        List<Emprestimo> emprestimos = emprestimoService.listarPorFuncionario(funcionarioId);
        return ResponseEntity.ok(emprestimos);
    }

    @Operation(summary = "Buscar empréstimo por ID", description = "Busca um empréstimo específico pelo ID")
    @GetMapping("/{id}")
    public ResponseEntity<Emprestimo> buscarPorId(@PathVariable Long id) {
        Emprestimo emprestimo = emprestimoService.buscarPorId(id);
        return ResponseEntity.ok(emprestimo);
    }

    @Operation(summary = "Listar todos os empréstimos", description = "Retorna todos os empréstimos do sistema")
    @GetMapping
    public ResponseEntity<List<Emprestimo>> listarTodos() {
        List<Emprestimo> emprestimos = emprestimoService.listarTodos();
        return ResponseEntity.ok(emprestimos);
    }

    public record EmprestimoRequest(
            @NotNull Long clienteId,
            @NotNull Long livroId,
            @NotNull Long funcionarioId
    ) {}

    public record DevolucaoRequest(@NotNull Long funcionarioId) {}
}