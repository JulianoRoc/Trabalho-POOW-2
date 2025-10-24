package br.csi.sistema_biblioteca.model.cliente;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    // Métodos por UUID (seguindo o padrão anterior)
    Optional<Cliente> findByUuid(UUID uuid);
    void deleteByUuid(UUID uuid);

    // Métodos originais mantidos
    Optional<Cliente> findByCpf(String cpf);

    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.cliente.id = :idCliente AND e.dataDevolucao IS NULL")
    boolean hasEmprestimosAtivos(@Param("idCliente") Long idCliente);

    // Nova consulta por UUID para empréstimos ativos
    @Query("SELECT COUNT(e) > 0 FROM Emprestimo e WHERE e.cliente.uuid = :uuidCliente AND e.dataDevolucao IS NULL")
    boolean hasEmprestimosAtivosByUuid(@Param("uuidCliente") UUID uuidCliente);
}