package br.csi.sistema_biblioteca.model.livro_categoria;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "categoria")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Entidade que representa uma categoria de livros no sistema da biblioteca")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_categoria")
    @Schema(description = "ID único da categoria", example = "1")
    private Long id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "uuid", unique = true, nullable = false, updatable = false, columnDefinition = "UUID")
    @Schema(description = "Código UUID único da categoria", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid = UUID.randomUUID();

    @NonNull
    @NotBlank(message = "Nome da categoria é obrigatório")
    @Size(max = 50, message = "Nome da categoria deve ter no máximo 50 caracteres")
    @Column(unique = true)
    @Schema(description = "Nome único da categoria", example = "Romance")
    private String nome;

    @Schema(description = "Descrição detalhada da categoria", example = "Livros de narrativas ficcionais sobre relacionamentos e experiências emocionais")
    private String descricao;

    @ManyToMany(mappedBy = "categorias")
    @Schema(description = "Conjunto de livros associados a esta categoria")
    private Set<Livro> livros = new HashSet<>();

    @PrePersist
    protected void gerarUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}