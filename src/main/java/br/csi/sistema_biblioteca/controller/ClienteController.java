package br.csi.sistema_biblioteca.controller;

import br.csi.sistema_biblioteca.model.cliente.Cliente;
import br.csi.sistema_biblioteca.model.cliente.ClienteRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/clientes")
@RequiredArgsConstructor
@Tag(name = "Clientes", description = "Path relacionado a operações de clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;

    @Operation(summary = "Listar todos os clientes", description = "Retorna todos os clientes cadastrados")
    @GetMapping
    public ResponseEntity<List<Cliente>> listarTodos() {
        List<Cliente> clientes = clienteRepository.findAll();
        return ResponseEntity.ok(clientes);
    }

    @Operation(summary = "Buscar cliente por ID", description = "Busca um cliente pelo ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Cliente> buscarPorId(@Parameter(description = "ID do cliente") @PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Buscar cliente por UUID", description = "Busca um cliente pelo UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/uuid/{uuid}")
    public ResponseEntity<Cliente> buscarPorUuid(@Parameter(description = "UUID do cliente") @PathVariable UUID uuid) {
        Cliente cliente = clienteRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Buscar cliente por CPF", description = "Busca um cliente pelo CPF")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @GetMapping("/cpf/{cpf}")
    public ResponseEntity<Cliente> buscarPorCpf(@Parameter(description = "CPF do cliente") @PathVariable String cpf) {
        Cliente cliente = clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
        return ResponseEntity.ok(cliente);
    }

    @Operation(summary = "Cadastrar cliente", description = "Cria um novo cliente no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
    })
    @PostMapping
    public ResponseEntity<Cliente> salvar(@RequestBody @Valid Cliente cliente,
                                          UriComponentsBuilder uriBuilder) {
        // Validação de CPF único
        if (clienteRepository.findByCpf(cliente.getCpf()).isPresent()) {
            throw new RuntimeException("CPF já cadastrado");
        }

        Cliente clienteSalvo = clienteRepository.save(cliente);
        var uri = uriBuilder.path("/clientes/{id}").buildAndExpand(clienteSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(clienteSalvo);
    }

    @Operation(summary = "Atualizar cliente por ID", description = "Atualiza os dados de um cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/{id}")
    public ResponseEntity<Cliente> atualizar(@Parameter(description = "ID do cliente") @PathVariable Long id,
                                             @RequestBody @Valid Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verificar se o CPF já existe em outro cliente
        clienteRepository.findByCpf(cliente.getCpf()).ifPresent(clienteComCpf -> {
            if (!clienteComCpf.getId().equals(id)) {
                throw new RuntimeException("CPF já cadastrado para outro cliente");
            }
        });

        // Atualizar campos
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setCpf(cliente.getCpf());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteExistente.setEndereco(cliente.getEndereco());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Operation(summary = "Atualizar cliente por UUID", description = "Atualiza os dados de um cliente existente usando UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente atualizado com sucesso",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Cliente.class))),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @PutMapping("/uuid")
    public ResponseEntity<Cliente> atualizarPorUuid(@RequestBody @Valid Cliente cliente) {
        Cliente clienteExistente = clienteRepository.findByUuid(cliente.getUuid())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verificar se o CPF já existe em outro cliente
        clienteRepository.findByCpf(cliente.getCpf()).ifPresent(clienteComCpf -> {
            if (!clienteComCpf.getUuid().equals(cliente.getUuid())) {
                throw new RuntimeException("CPF já cadastrado para outro cliente");
            }
        });

        // Atualizar campos
        clienteExistente.setNome(cliente.getNome());
        clienteExistente.setCpf(cliente.getCpf());
        clienteExistente.setTelefone(cliente.getTelefone());
        clienteExistente.setEndereco(cliente.getEndereco());

        Cliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return ResponseEntity.ok(clienteAtualizado);
    }

    @Operation(summary = "Excluir cliente", description = "Remove permanentemente um cliente do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Cliente possui empréstimos ativos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@Parameter(description = "ID do cliente") @PathVariable Long id) {
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verificar se o cliente possui empréstimos ativos
        if (clienteRepository.hasEmprestimosAtivos(id)) {
            throw new RuntimeException("Cliente possui empréstimos ativos e não pode ser excluído");
        }

        clienteRepository.delete(cliente);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Excluir cliente por UUID", description = "Remove permanentemente um cliente usando UUID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Cliente excluído com sucesso"),
            @ApiResponse(responseCode = "400", description = "Cliente possui empréstimos ativos"),
            @ApiResponse(responseCode = "404", description = "Cliente não encontrado")
    })
    @DeleteMapping("/uuid/{uuid}")
    public ResponseEntity<Void> excluirPorUuid(@Parameter(description = "UUID do cliente") @PathVariable UUID uuid) {
        Cliente cliente = clienteRepository.findByUuid(uuid)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        // Verificar se o cliente possui empréstimos ativos
        if (clienteRepository.hasEmprestimosAtivosByUuid(uuid)) {
            throw new RuntimeException("Cliente possui empréstimos ativos e não pode ser excluído");
        }

        clienteRepository.delete(cliente);
        return ResponseEntity.noContent().build();
    }
}