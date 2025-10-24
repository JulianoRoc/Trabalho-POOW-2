package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.livro_categoria.Livro;
import br.csi.sistema_biblioteca.service.LivroService;
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
@RequestMapping("/livros")
@RequiredArgsConstructor
@Tag(name = "Livros", description = "Operações relacionadas ao gerenciamento de livros")
public class LivroController {

    private final LivroService livroService;

    @Operation(summary = "Listar todos os livros", description = "Retorna todos os livros cadastrados no sistema")
    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        List<Livro> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Listar livros disponíveis", description = "Retorna apenas os livros disponíveis para empréstimo")
    @GetMapping("/disponiveis")
    public ResponseEntity<List<Livro>> listarDisponiveis() {
        List<Livro> livros = livroService.listarDisponiveis();
        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Buscar livro por ID", description = "Busca um livro pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@Parameter(description = "ID do livro") @PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Buscar livro por UUID", description = "Busca um livro pelo UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Livro> buscarPorUuid(@Parameter(description = "UUID público do livro") @PathVariable UUID uuid) {
        Livro livro = livroService.buscarPorUuid(uuid);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Buscar livro disponível por UUID", description = "Busca um livro disponível pelo UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro disponível encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado ou indisponível")
    })
    @GetMapping("/uuid/{uuid}/disponivel")
    public ResponseEntity<Livro> buscarPorUuidDisponivel(@Parameter(description = "UUID público do livro") @PathVariable UUID uuid) {
        Livro livro = livroService.buscarPorUuidDisponivel(uuid);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Buscar livros por título", description = "Busca livros por título (busca parcial)")
    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Livro>> buscarPorTitulo(@Parameter(description = "Título para busca") @RequestParam String titulo) {
        List<Livro> livros = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Buscar livros disponíveis por título", description = "Busca livros disponíveis por título (busca parcial)")
    @GetMapping("/buscar/titulo/disponiveis")
    public ResponseEntity<List<Livro>> buscarPorTituloDisponiveis(@Parameter(description = "Título para busca") @RequestParam String titulo) {
        List<Livro> livros = livroService.buscarPorTituloDisponiveis(titulo);
        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Buscar livros por autor", description = "Busca livros por autor (busca parcial)")
    @GetMapping("/buscar/autor")
    public ResponseEntity<List<Livro>> buscarPorAutor(@Parameter(description = "Autor para busca") @RequestParam String autor) {
        List<Livro> livros = livroService.buscarPorAutor(autor);
        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Buscar livros disponíveis por autor", description = "Busca livros disponíveis por autor (busca parcial)")
    @GetMapping("/buscar/autor/disponiveis")
    public ResponseEntity<List<Livro>> buscarPorAutorDisponiveis(@Parameter(description = "Autor para busca") @RequestParam String autor) {
        List<Livro> livros = livroService.buscarPorAutorDisponiveis(autor);
        return ResponseEntity.ok(livros);
    }

    @Operation(summary = "Cadastrar livro", description = "Adiciona um novo livro ao acervo")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livro cadastrado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<Livro> salvar(@RequestBody @Valid Livro livro,
                                        UriComponentsBuilder uriBuilder) {
        Livro livroSalvo = livroService.salvar(livro);
        var uri = uriBuilder.path("/livros/{id}").buildAndExpand(livroSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(livroSalvo);
    }

    @Operation(summary = "Atualizar livro por ID", description = "Atualiza os dados de um livro existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@Parameter(description = "ID do livro") @PathVariable Long id,
                                           @RequestBody @Valid Livro livro) {
        Livro livroAtualizado = livroService.atualizar(id, livro);
        return ResponseEntity.ok(livroAtualizado);
    }

    @Operation(summary = "Atualizar livro por UUID", description = "Atualiza os dados de um livro existente usando UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Livro atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Livro.class))),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @PutMapping("/uuid/{uuid}")
    public ResponseEntity<Livro> atualizarPorUuid(@Parameter(description = "UUID público do livro") @PathVariable UUID uuid,
                                                  @RequestBody @Valid Livro livro) {
        Livro livroAtualizado = livroService.atualizarPorUuid(uuid, livro);
        return ResponseEntity.ok(livroAtualizado);
    }

    @Operation(summary = "Excluir livro", description = "Remove permanentemente um livro do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Livro está emprestado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do livro") @PathVariable Long id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir livro por UUID", description = "Remove permanentemente um livro do sistema usando UUID público")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Livro excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Livro está emprestado"),
            @ApiResponse(responseCode = "404", description = "Livro não encontrado")
    })
    @DeleteMapping("/uuid/{uuid}")
    public ResponseEntity<Void> excluirPorUuid(@Parameter(description = "UUID público do livro") @PathVariable UUID uuid) {
        livroService.excluirPorUuid(uuid);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Adicionar categoria ao livro", description = "Associa uma categoria a um livro")
    @PostMapping("/{idLivro}/categorias/{idCategoria}")
    public ResponseEntity<Livro> adicionarCategoria(@PathVariable Long idLivro, @PathVariable Long idCategoria) {
        Livro livro = livroService.adicionarCategoria(idLivro, idCategoria);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Adicionar categoria ao livro por UUID", description = "Associa uma categoria a um livro usando UUID público")
    @PostMapping("/uuid/{uuidLivro}/categorias/{idCategoria}")
    public ResponseEntity<Livro> adicionarCategoriaPorUuid(@PathVariable UUID uuidLivro, @PathVariable Long idCategoria) {
        Livro livro = livroService.adicionarCategoriaPorUuid(uuidLivro, idCategoria);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Remover categoria do livro", description = "Remove a associação de uma categoria com um livro")
    @DeleteMapping("/{idLivro}/categorias/{idCategoria}")
    public ResponseEntity<Livro> removerCategoria(@PathVariable Long idLivro, @PathVariable Long idCategoria) {
        Livro livro = livroService.removerCategoria(idLivro, idCategoria);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Remover categoria do livro por UUID", description = "Remove a associação de uma categoria com um livro usando UUID público")
    @DeleteMapping("/uuid/{uuidLivro}/categorias/{idCategoria}")
    public ResponseEntity<Livro> removerCategoriaPorUuid(@PathVariable UUID uuidLivro, @PathVariable Long idCategoria) {
        Livro livro = livroService.removerCategoriaPorUuid(uuidLivro, idCategoria);
        return ResponseEntity.ok(livro);
    }

    @Operation(summary = "Buscar livros por categoria", description = "Retorna todos os livros de uma categoria específica")
    @GetMapping("/categoria/{idCategoria}")
    public ResponseEntity<List<Livro>> buscarPorCategoria(@PathVariable Long idCategoria) {
        List<Livro> livros = livroService.buscarPorCategoria(idCategoria);
        return ResponseEntity.ok(livros);
    }
}
