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
@Table(name = "livro")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Entidade que representa um livro no sistema da biblioteca")
public class Livro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_livro")
    @Schema(description = "ID único do livro", example = "1")
    private Long id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "uuid", unique = true, nullable = false, updatable = false, columnDefinition = "UUID")
    @Schema(description = "Código UUID único do livro", example = "123e4567-e89b-12d3-a456-426614174000")
    private UUID uuid = UUID.randomUUID();

    @NonNull
    @NotBlank(message = "Título é obrigatório")
    @Size(max = 200, message = "Título deve ter no máximo 200 caracteres")
    @Schema(description = "Título completo do livro", example = "Dom Casmurro")
    private String titulo;

    @NonNull
    @NotBlank(message = "Autor é obrigatório")
    @Size(max = 100, message = "Nome do autor deve ter no máximo 100 caracteres")
    @Schema(description = "Nome do autor do livro", example = "Machado de Assis")
    private String autor;

    @Column(name = "ano_publicacao")
    @Schema(description = "Ano de publicação do livro", example = "1899")
    private Integer anoPublicacao;

    @Schema(description = "Status de disponibilidade do livro para empréstimo", example = "true")
    private Boolean disponivel = true;

    @ManyToMany
    @JoinTable(
            name = "livro_categoria",
            joinColumns = @JoinColumn(name = "id_livro"),
            inverseJoinColumns = @JoinColumn(name = "id_categoria")
    )
    @Schema(description = "Conjunto de categorias associadas ao livro")
    private Set<Categoria> categorias = new HashSet<>();

    @PrePersist
    protected void gerarUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }

    public void adicionarCategoria(Categoria categoria) {
        this.categorias.add(categoria);
        categoria.getLivros().add(this);
    }

    public void removerCategoria(Categoria categoria) {
        this.categorias.remove(categoria);
        categoria.getLivros().remove(this);
    }
}
