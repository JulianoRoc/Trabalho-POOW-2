package br.csi.sistema_biblioteca.repository;

import br.csi.sistema_biblioteca.model.Livro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByDisponivelTrue();

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByAutorContainingIgnoreCase(String autor);

    @Query("SELECT l FROM Livro l WHERE l.disponivel = true AND l.id = :id")
    Optional<Livro> findByIdAndDisponivelTrue(Long id);
}
