package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.emprestimo.Emprestimo;
import br.csi.sistema_biblioteca.model.emprestimo.EmprestimoRepository;
import br.csi.sistema_biblioteca.model.livro_categoria.Categoria;
import br.csi.sistema_biblioteca.model.livro_categoria.Livro;
import br.csi.sistema_biblioteca.model.livro_categoria.LivroRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LivroService {

    private final LivroRepository livroRepository;
    private final CategoriaService categoriaService;
    private final EmprestimoRepository emprestimoRepository;

    public Livro salvar(Livro livro) {
        return this.livroRepository.save(livro);
    }

    public List<Livro> listarTodos() {
        return this.livroRepository.findAll();
    }

    public List<Livro> listarDisponiveis() {
        return this.livroRepository.findByDisponivelTrue();
    }

    public Livro buscarPorId(Long id) {
        return this.livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    public Livro buscarPorUuid(UUID uuid) {
        return this.livroRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));
    }

    public Livro buscarPorUuidDisponivel(UUID uuid) {
        return this.livroRepository.findByUuidAndDisponivelTrue(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado ou indisponível"));
    }

    public void excluir(Long id) {
        Livro livro = buscarPorId(id);

        Optional<Emprestimo> emprestimoAtivo = emprestimoRepository.findEmprestimoAtivoPorLivro(id);
        if (emprestimoAtivo.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro está emprestado e não pode ser excluído");
        }

        this.livroRepository.delete(livro);
    }

    public void excluirPorUuid(UUID uuid) {
        Livro livro = buscarPorUuid(uuid);

        Optional<Emprestimo> emprestimoAtivo = emprestimoRepository.findEmprestimoAtivoPorLivro(livro.getId());
        if (emprestimoAtivo.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Livro está emprestado e não pode ser excluído");
        }

        this.livroRepository.delete(livro);
    }

    public Livro atualizar(Long id, Livro livro) {
        Livro l = this.livroRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));

        l.setTitulo(livro.getTitulo());
        l.setAutor(livro.getAutor());
        l.setAnoPublicacao(livro.getAnoPublicacao());
        l.setDisponivel(livro.getDisponivel());

        // 🔥 ATUALIZAR CATEGORIAS
        if (livro.getCategorias() != null) {
            atualizarCategoriasDoLivro(l, livro.getCategorias());
        }

        return this.livroRepository.save(l);
    }

    public Livro atualizarPorUuid(UUID uuid, Livro livro) {
        Livro l = this.livroRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Livro não encontrado"));

        l.setTitulo(livro.getTitulo());
        l.setAutor(livro.getAutor());
        l.setAnoPublicacao(livro.getAnoPublicacao());
        l.setDisponivel(livro.getDisponivel());

        // 🔥 ATUALIZAR CATEGORIAS
        if (livro.getCategorias() != null) {
            atualizarCategoriasDoLivro(l, livro.getCategorias());
        }

        return this.livroRepository.save(l);
    }

    // 🔥 MÉTODO AUXILIAR PARA ATUALIZAR CATEGORIAS
    private void atualizarCategoriasDoLivro(Livro livroExistente, Set<Categoria> novasCategorias) {
        // Limpa as categorias atuais
        livroExistente.getCategorias().clear();

        // Busca e adiciona as novas categorias (garantindo que são entidades gerenciadas)
        Set<Categoria> categoriasGerenciadas = new HashSet<>();
        for (Categoria categoria : novasCategorias) {
            Categoria categoriaGerenciada = categoriaService.buscarPorId(categoria.getId());
            categoriasGerenciadas.add(categoriaGerenciada);
        }

        livroExistente.getCategorias().addAll(categoriasGerenciadas);
    }

    // ... outros métodos permanecem iguais
    public List<Livro> buscarPorTitulo(String titulo) {
        return this.livroRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Livro> buscarPorTituloDisponiveis(String titulo) {
        return this.livroRepository.findByTituloContainingIgnoreCaseAndDisponivelTrue(titulo);
    }

    public List<Livro> buscarPorAutor(String autor) {
        return this.livroRepository.findByAutorContainingIgnoreCase(autor);
    }

    public List<Livro> buscarPorAutorDisponiveis(String autor) {
        return this.livroRepository.findByAutorContainingIgnoreCaseAndDisponivelTrue(autor);
    }

    public Livro adicionarCategoria(Long livroId, Long categoriaId) {
        Livro livro = buscarPorId(livroId);
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        livro.adicionarCategoria(categoria);
        return this.livroRepository.save(livro);
    }

    public Livro adicionarCategoriaPorUuid(UUID uuidLivro, Long categoriaId) {
        Livro livro = buscarPorUuid(uuidLivro);
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        livro.adicionarCategoria(categoria);
        return this.livroRepository.save(livro);
    }

    public Livro removerCategoria(Long livroId, Long categoriaId) {
        Livro livro = buscarPorId(livroId);
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        livro.removerCategoria(categoria);
        return this.livroRepository.save(livro);
    }

    public Livro removerCategoriaPorUuid(UUID uuidLivro, Long categoriaId) {
        Livro livro = buscarPorUuid(uuidLivro);
        Categoria categoria = categoriaService.buscarPorId(categoriaId);

        livro.removerCategoria(categoria);
        return this.livroRepository.save(livro);
    }

    public List<Livro> buscarPorCategoria(Long categoriaId) {
        Categoria categoria = categoriaService.buscarPorId(categoriaId);
        return new ArrayList<>(categoria.getLivros());
    }

    public boolean existePorUuid(UUID uuid) {
        return this.livroRepository.existsByUuid(uuid);
    }
}