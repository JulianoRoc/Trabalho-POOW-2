package br.csi.sistema_biblioteca.repository;


import br.csi.sistema_biblioteca.model.Emprestimo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

    List<Emprestimo> findByClienteId(Long clienteId);

    List<Emprestimo> findByDataDevolucaoIsNull();

    @Query("SELECT e FROM Emprestimo e WHERE e.livro.id = :livroId AND e.dataDevolucao IS NULL")
    Optional<Emprestimo> findEmprestimoAtivoPorLivro(@Param("livroId") Long livroId);

    @Query("SELECT COUNT(e) FROM Emprestimo e WHERE e.cliente.id = :clienteId AND e.dataDevolucao IS NULL")
    int countEmprestimosAtivosPorCliente(@Param("clienteId") Long clienteId);
}
