package br.csi.sistema_biblioteca.model.emprestimo;

import br.csi.sistema_biblioteca.model.cliente.Cliente;
import br.csi.sistema_biblioteca.model.funcionario.Funcionario;
import br.csi.sistema_biblioteca.model.livro_categoria.Livro;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "emprestimo")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa um empréstimo de livro no sistema da biblioteca")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_emprestimo")
    @Schema(description = "ID único do empréstimo", example = "1")
    private Long id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "uuid", unique = true, nullable = false, updatable = false, columnDefinition = "UUID")
    @Schema(description = "Código UUID único do empréstimo", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid = UUID.randomUUID();

    @Column(name = "data_emprestimo")
    @Schema(description = "Data e hora do empréstimo", example = "2024-01-15T10:30:00")
    private LocalDateTime dataEmprestimo = LocalDateTime.now();

    @Column(name = "data_devolucao")
    @Schema(description = "Data e hora da devolução", example = "2024-01-20T14:25:00")
    private LocalDateTime dataDevolucao;

    @Column(name = "data_devolucao_prevista")
    @Schema(description = "Data prevista para devolução do livro", example = "2024-01-29T10:30:00")
    private LocalDateTime dataDevolucaoPrevista;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    @Schema(description = "Cliente que realizou o empréstimo")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_livro")
    @Schema(description = "Livro que foi emprestado")
    private Livro livro;

    @ManyToOne
    @JoinColumn(name = "id_funcionario")
    @Schema(description = "Funcionário que registrou o empréstimo")
    private Funcionario funcionario;

    @PrePersist
    protected void gerarUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    public Emprestimo(Cliente cliente, Livro livro, Funcionario funcionario) {
        this.cliente = cliente;
        this.livro = livro;
        this.funcionario = funcionario;
        this.dataEmprestimo = LocalDateTime.now();
        this.dataDevolucaoPrevista = LocalDateTime.now().plusDays(14);
    }
}
