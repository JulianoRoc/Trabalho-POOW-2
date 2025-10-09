package br.csi.sistema_biblioteca.repository;

import br.csi.sistema_biblioteca.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.cliente.id = :idCliente AND e.dataDevolucao IS NULL")
    boolean hasEmprestimosAtivos(@Param("idCliente") Long idCliente);
}
