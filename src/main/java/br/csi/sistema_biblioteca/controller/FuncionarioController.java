package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.funcionario.Funcionario;
import br.csi.sistema_biblioteca.service.FuncionarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/funcionarios")
@RequiredArgsConstructor
@Tag(name = "Funcionários", description = "Path relacionado a operações de funcionários")
public class FuncionarioController {

    private final FuncionarioService funcionarioService;

    @Operation(summary = "Listar funcionários ativos", description = "Retorna apenas os funcionários ativos no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionários ativos listados com sucesso")
    })
    @GetMapping("/ativos")
    public ResponseEntity<List<Funcionario>> listarAtivos() {
        List<Funcionario> funcionarios = funcionarioService.listarAtivos();
        return ResponseEntity.ok(funcionarios);
    }

    @Operation(summary = "Buscar funcionário por ID", description = "Busca um funcionário pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Funcionario> buscarPorId(@Parameter(description = "ID do funcionário") @PathVariable Long id) {
        Funcionario funcionario = funcionarioService.buscarPorId(id);
        return ResponseEntity.ok(funcionario);
    }

    @Operation(summary = "Buscar funcionário por UUID", description = "Busca um funcionário pelo UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Funcionario> buscarPorUuid(@Parameter(description = "UUID do funcionário") @PathVariable UUID uuid) {
        Funcionario funcionario = funcionarioService.buscarPorUuid(uuid);
        return ResponseEntity.ok(funcionario);
    }

    @Operation(summary = "Criar novo funcionário", description = "Cadastra um novo funcionário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Funcionário criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<Funcionario> salvar(@RequestBody @Valid Funcionario funcionario,
                                              UriComponentsBuilder uriBuilder) {
        Funcionario funcionarioSalvo = funcionarioService.salvar(funcionario);
        var uri = uriBuilder.path("/funcionarios/{id}").buildAndExpand(funcionarioSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(funcionarioSalvo);
    }

    @Operation(summary = "Atualizar funcionário por ID", description = "Atualiza os dados de um funcionário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Funcionario> atualizar(@Parameter(description = "ID do funcionário") @PathVariable Long id,
                                                 @RequestBody @Valid Funcionario funcionario) {
        Funcionario funcionarioAtualizado = funcionarioService.atualizar(id, funcionario);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @Operation(summary = "Atualizar funcionário por UUID", description = "Atualiza os dados de um funcionário existente usando UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionário atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Funcionario.class))),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PutMapping("/uuid")
    public ResponseEntity<Funcionario> atualizarPorUuid(@RequestBody @Valid Funcionario funcionario) {
        Funcionario funcionarioAtualizado = funcionarioService.atualizarPorUuid(funcionario);
        return ResponseEntity.ok(funcionarioAtualizado);
    }

    @Operation(summary = "Desativar funcionário", description = "Desativa um funcionário no sistema (exclusão lógica)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Void> desativar(@Parameter(description = "ID do funcionário") @PathVariable Long id) {
        funcionarioService.desativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Desativar funcionário por UUID", description = "Desativa um funcionário usando UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário desativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PatchMapping("/uuid/{uuid}/desativar")
    public ResponseEntity<Void> desativarPorUuid(@Parameter(description = "UUID do funcionário") @PathVariable UUID uuid) {
        funcionarioService.desativarPorUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar funcionário", description = "Reativa um funcionário previamente desativado")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Void> ativar(@Parameter(description = "ID do funcionário") @PathVariable Long id) {
        funcionarioService.ativar(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Ativar funcionário por UUID", description = "Reativa um funcionário usando UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário ativado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @PatchMapping("/uuid/{uuid}/ativar")
    public ResponseEntity<Void> ativarPorUuid(@Parameter(description = "UUID do funcionário") @PathVariable UUID uuid) {
        funcionarioService.ativarPorUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir funcionário", description = "Remove permanentemente um funcionário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do funcionário") @PathVariable Long id) {
        funcionarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir funcionário por UUID", description = "Remove permanentemente um funcionário usando UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Funcionário excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Funcionário não encontrado")
    })
    @DeleteMapping("/uuid/{uuid}")
    public ResponseEntity<Void> excluirPorUuid(@Parameter(description = "UUID do funcionário") @PathVariable UUID uuid) {
        funcionarioService.excluirPorUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os funcionários", description = "Retorna todos os funcionários cadastrados")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Funcionários listados com sucesso")
    })
    @GetMapping
    public ResponseEntity<List<Funcionario>> listarTodos() {
        List<Funcionario> funcionarios = funcionarioService.listarTodos();
        return ResponseEntity.ok(funcionarios);
    }
}
