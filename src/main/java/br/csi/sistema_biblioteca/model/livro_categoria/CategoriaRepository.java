package br.csi.sistema_biblioteca.model.livro_categoria;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {

    Optional<Categoria> findByNome(String nome);

    List<Categoria> findByNomeContainingIgnoreCase(String nome);

    @Query("SELECT COUNT(l) > 0 FROM Categoria c JOIN c.livros l WHERE c.id = :categoriaId")
    boolean hasLivrosAssociados(@Param("categoriaId") Long categoriaId);

    Optional<Categoria> findByNomeIgnoreCase(String nome);

    Optional<Categoria> findByUuid(UUID uuid);

    @Query("SELECT COUNT(l) > 0 FROM Categoria c JOIN c.livros l WHERE c.uuid = :categoriaUuid")
    boolean hasLivrosAssociadosByUuid(@Param("categoriaUuid") UUID categoriaUuid);

    @Query("SELECT COUNT(c) > 0 FROM Categoria c WHERE c.nome = :nome AND c.uuid != :uuid")
    boolean existsByNomeAndUuidNot(@Param("nome") String nome, @Param("uuid") UUID uuid);

    List<Categoria> findByUuidIn(List<UUID> uuids);

    boolean existsByUuid(UUID uuid);
}
