package br.csi.sistema_biblioteca.repository;

import br.csi.sistema_biblioteca.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    Optional<Funcionario> findByEmail(String email);

    @Query("SELECT f FROM Funcionario f WHERE f.id = :id AND f.ativo = true")
    Optional<Funcionario> findByIdAndAtivoTrue(@Param("id") Long id);

    Optional<Funcionario> findByEmailAndSenha(String email, String senha);
}
