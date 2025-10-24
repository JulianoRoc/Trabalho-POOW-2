package br.csi.sistema_biblioteca.model.livro_categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<Livro, Long> {

    List<Livro> findByDisponivelTrue();

    List<Livro> findByTituloContainingIgnoreCase(String titulo);

    List<Livro> findByAutorContainingIgnoreCase(String autor);

    @Query("SELECT l FROM Livro l WHERE l.disponivel = true AND l.id = :id")
    Optional<Livro> findByIdAndDisponivelTrue(@Param("id") Long id);

    List<Livro> findByTituloContainingIgnoreCaseAndDisponivelTrue(String titulo);
    List<Livro> findByAutorContainingIgnoreCaseAndDisponivelTrue(String autor);

    Optional<Livro> findByUuid(UUID uuid);

    @Query("SELECT l FROM Livro l WHERE l.disponivel = true AND l.uuid = :uuid")
    Optional<Livro> findByUuidAndDisponivelTrue(@Param("uuid") UUID uuid);

    List<Livro> findByUuidIn(List<UUID> uuids);

    @Query("SELECT l FROM Livro l WHERE l.disponivel = true AND l.uuid IN :uuids")
    List<Livro> findByUuidInAndDisponivelTrue(@Param("uuids") List<UUID> uuids);

    boolean existsByUuid(UUID uuid);
}