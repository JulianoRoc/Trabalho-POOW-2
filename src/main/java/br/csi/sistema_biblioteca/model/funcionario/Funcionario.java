package br.csi.sistema_biblioteca.model.funcionario;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "funcionario")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Entidade que representa um funcionário no sistema da biblioteca")
public class Funcionario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionario")
    @Schema(description = "ID único do funcionário", example = "1")
    private Long id;

    @UuidGenerator
    @Schema(description = "UUID único do funcionário para identificação externa")
    private UUID uuid;

    @NonNull
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Schema(description = "Nome completo do funcionário", example = "João Silva")
    private String nome;

    @NonNull
    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email deve ser válido")
    @Column(unique = true)
    @Schema(description = "E-mail único do funcionário", example = "joao.silva@biblioteca.com")
    private String email;

    @NonNull
    @NotBlank(message = "Senha é obrigatória")
    @Schema(description = "Senha de acesso do funcionário")
    private String senha;

    @Schema(description = "Status de atividade do funcionário no sistema", example = "true")
    private Boolean ativo = true;
}
