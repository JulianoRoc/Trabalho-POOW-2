package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.livro_categoria.Categoria;
import br.csi.sistema_biblioteca.model.livro_categoria.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public Categoria salvar(Categoria categoria) {
        if (categoriaRepository.findByNomeIgnoreCase(categoria.getNome()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma categoria com este nome");
        }
        return this.categoriaRepository.save(categoria);
    }

    public List<Categoria> listarTodas() {
        return this.categoriaRepository.findAll();
    }

    public Categoria buscarPorId(Long id) {
        return this.categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public Categoria buscarPorUuid(UUID uuid) {
        return this.categoriaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public void excluir(Long id) {
        Categoria categoria = buscarPorId(id);

        // Verificar se a categoria possui livros associados
        if (categoriaRepository.hasLivrosAssociados(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria possui livros associados e não pode ser excluída");
        }

        this.categoriaRepository.delete(categoria);
    }

    public void excluirPorUuid(UUID uuid) {
        Categoria categoria = buscarPorUuid(uuid);

        // Verificar se a categoria possui livros associados
        if (categoriaRepository.hasLivrosAssociadosByUuid(uuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Categoria possui livros associados e não pode ser excluída");
        }

        this.categoriaRepository.delete(categoria);
    }

    public Categoria atualizar(Long id, Categoria categoria) {
        Categoria c = this.categoriaRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        // Verificar se o nome já existe em outra categoria
        categoriaRepository.findByNomeIgnoreCase(categoria.getNome()).ifPresent(categoriaComNome -> {
            if (!categoriaComNome.getId().equals(id)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma categoria com este nome");
            }
        });

        c.setNome(categoria.getNome());
        c.setDescricao(categoria.getDescricao());

        return this.categoriaRepository.save(c);
    }

    public Categoria atualizarPorUuid(UUID uuid, Categoria categoria) {
        Categoria c = this.categoriaRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));

        // Verificar se o nome já existe em outra categoria
        if (categoriaRepository.existsByNomeAndUuidNot(categoria.getNome(), uuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Já existe uma categoria com este nome");
        }

        c.setNome(categoria.getNome());
        c.setDescricao(categoria.getDescricao());

        return this.categoriaRepository.save(c);
    }

    public Categoria buscarPorNome(String nome) {
        return this.categoriaRepository.findByNomeIgnoreCase(nome)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Categoria não encontrada"));
    }

    public List<Categoria> buscarPorNomeParcial(String nome) {
        return this.categoriaRepository.findByNomeContainingIgnoreCase(nome);
    }
}