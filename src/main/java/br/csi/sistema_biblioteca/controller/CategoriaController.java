package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.livro_categoria.Categoria;
import br.csi.sistema_biblioteca.service.CategoriaService;
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
@RequestMapping("/categorias")
@RequiredArgsConstructor
@Tag(name = "Categorias", description = "Path relacionado a operações de categorias de livros")
public class CategoriaController {

    private final CategoriaService categoriaService;

    @Operation(summary = "Listar todas as categorias", description = "Retorna todas as categorias cadastradas")
    @GetMapping
    public ResponseEntity<List<Categoria>> listarTodas() {
        List<Categoria> categorias = categoriaService.listarTodas();
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar categoria por ID", description = "Busca uma categoria pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Categoria> buscarPorId(@Parameter(description = "ID da categoria") @PathVariable Long id) {
        Categoria categoria = categoriaService.buscarPorId(id);
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Buscar categoria por UUID", description = "Busca uma categoria pelo UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Categoria> buscarPorUuid(@Parameter(description = "UUID público da categoria") @PathVariable UUID uuid) {
        Categoria categoria = categoriaService.buscarPorUuid(uuid);
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Buscar categorias por nome", description = "Busca categorias por nome (busca parcial)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categorias encontradas")
    })
    @GetMapping("/buscar")
    public ResponseEntity<List<Categoria>> buscarPorNome(@Parameter(description = "Nome da categoria para busca") @RequestParam String nome) {
        List<Categoria> categorias = categoriaService.buscarPorNomeParcial(nome);
        return ResponseEntity.ok(categorias);
    }

    @Operation(summary = "Buscar categoria por nome exato", description = "Busca uma categoria pelo nome exato")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria encontrada",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @GetMapping("/nome/{nome}")
    public ResponseEntity<Categoria> buscarPorNomeExato(@Parameter(description = "Nome exato da categoria") @PathVariable String nome) {
        Categoria categoria = categoriaService.buscarPorNome(nome);
        return ResponseEntity.ok(categoria);
    }

    @Operation(summary = "Cadastrar categoria", description = "Cria uma nova categoria no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Categoria criada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<Categoria> salvar(@RequestBody @Valid Categoria categoria,
                                            UriComponentsBuilder uriBuilder) {
        Categoria categoriaSalva = categoriaService.salvar(categoria);
        var uri = uriBuilder.path("/categorias/{id}").buildAndExpand(categoriaSalva.getId()).toUri();
        return ResponseEntity.created(uri).body(categoriaSalva);
    }

    @Operation(summary = "Atualizar categoria por ID", description = "Atualiza os dados de uma categoria existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Categoria> atualizar(@Parameter(description = "ID da categoria") @PathVariable Long id,
                                               @RequestBody @Valid Categoria categoria) {
        Categoria categoriaAtualizada = categoriaService.atualizar(id, categoria);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @Operation(summary = "Atualizar categoria por UUID", description = "Atualiza os dados de uma categoria existente usando UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Categoria atualizada com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Categoria.class))),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @PutMapping("/uuid/{uuid}")
    public ResponseEntity<Categoria> atualizarPorUuid(@Parameter(description = "UUID público da categoria") @PathVariable UUID uuid,
                                                      @RequestBody @Valid Categoria categoria) {
        Categoria categoriaAtualizada = categoriaService.atualizarPorUuid(uuid, categoria);
        return ResponseEntity.ok(categoriaAtualizada);
    }

    @Operation(summary = "Excluir categoria", description = "Remove permanentemente uma categoria do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria possui livros associados"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID da categoria") @PathVariable Long id) {
        categoriaService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir categoria por UUID", description = "Remove permanentemente uma categoria do sistema usando UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Categoria excluída com sucesso"),
            @ApiResponse(responseCode = "400", description = "Categoria possui livros associados"),
            @ApiResponse(responseCode = "404", description = "Categoria não encontrada")
    })
    @DeleteMapping("/uuid/{uuid}")
    public ResponseEntity<Void> excluirPorUuid(@Parameter(description = "UUID público da categoria") @PathVariable UUID uuid) {
        categoriaService.excluirPorUuid(uuid);
        return ResponseEntity.noContent().build();
    }
}
