package br.csi.sistema_biblioteca.model.funcionario;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);

    Optional<Funcionario> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("SELECT f FROM Funcionario f WHERE f.id = :id AND f.ativo = true")
    Optional<Funcionario> findByIdAndAtivoTrue(@Param("id") Long id);

    @Query("SELECT f FROM Funcionario f WHERE f.uuid = :uuid AND f.ativo = true")
    Optional<Funcionario> findByUuidAndAtivoTrue(@Param("uuid") UUID uuid);

    List<Funcionario> findByAtivoTrue();
}