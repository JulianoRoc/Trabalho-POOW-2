package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.Livro;
import br.csi.sistema_biblioteca.service.LivroService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/livros")
@RequiredArgsConstructor
public class LivroController {

    private final LivroService livroService;

    @PostMapping
    public ResponseEntity<Livro> salvar(@RequestBody @Valid Livro livro,
                                        UriComponentsBuilder uriBuilder) {
        Livro livroSalvo = livroService.salvar(livro);
        var uri = uriBuilder.path("/livros/{id}").buildAndExpand(livroSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(livroSalvo);
    }

    @GetMapping
    public ResponseEntity<List<Livro>> listarTodos() {
        List<Livro> livros = livroService.listarTodos();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/disponiveis")
    public ResponseEntity<List<Livro>> listarDisponiveis() {
        List<Livro> livros = livroService.listarDisponiveis();
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Livro> buscarPorId(@PathVariable Long id) {
        Livro livro = livroService.buscarPorId(id);
        return ResponseEntity.ok(livro);
    }

    @GetMapping("/buscar/titulo")
    public ResponseEntity<List<Livro>> buscarPorTitulo(@RequestParam String titulo) {
        List<Livro> livros = livroService.buscarPorTitulo(titulo);
        return ResponseEntity.ok(livros);
    }

    @GetMapping("/buscar/autor")
    public ResponseEntity<List<Livro>> buscarPorAutor(@RequestParam String autor) {
        List<Livro> livros = livroService.buscarPorAutor(autor);
        return ResponseEntity.ok(livros);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Livro> atualizar(@PathVariable Long id,
                                           @RequestBody @Valid Livro livro) {
        Livro livroAtualizado = livroService.atualizar(id, livro);
        return ResponseEntity.ok(livroAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        livroService.excluir(id);
        return ResponseEntity.noContent().build();
    }
}