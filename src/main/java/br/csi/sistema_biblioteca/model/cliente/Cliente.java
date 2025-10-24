package br.csi.sistema_biblioteca.model.cliente;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "cliente")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
@Schema(description = "Representa um cliente da biblioteca")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_cliente")
    @Schema(description = "ID único do cliente", example = "1")
    private Long id;

    @UuidGenerator
    @Schema(description = "UUID único do cliente para identificação externa")
    private UUID uuid;

    @NonNull
    @NotBlank(message = "Nome é obrigatório")
    @Size(max = 100, message = "Nome deve ter no máximo 100 caracteres")
    @Schema(description = "Nome completo do cliente", example = "Maria Santos")
    private String nome;

    @NonNull
    @NotBlank(message = "CPF é obrigatório")
    @Size(min = 11, max = 11, message = "CPF deve ter exatamente 11 caracteres")
    @Column(unique = true)
    @Schema(description = "CPF único do cliente (apenas números)", example = "12345678901")
    private String cpf;

    @Size(max = 15, message = "Telefone deve ter no máximo 15 caracteres")
    @Schema(description = "Número de telefone do cliente", example = "(11) 99999-9999")
    private String telefone;

    @Schema(description = "Endereço completo do cliente", example = "Rua das Flores, 123 - Centro - São Paulo/SP")
    private String endereco;
}
