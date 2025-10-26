package br.csi.sistema_biblioteca.service;

import br.csi.sistema_biblioteca.model.cliente.Cliente;
import br.csi.sistema_biblioteca.model.cliente.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public void salvar(Cliente cliente) {
        Optional<Cliente> clienteExistente = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteExistente.isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado");
        }
        this.clienteRepository.save(cliente);
    }

    public List<Cliente> listarTodos() {
        return this.clienteRepository.findAll();
    }

    public Cliente buscarPorId(Long id) {
        return this.clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    public Cliente buscarPorUuid(UUID uuid) {
        return this.clienteRepository.findByUuid(uuid)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    public void excluir(Long id) {
        Cliente cliente = buscarPorId(id);

        // Verificar se cliente tem empréstimos ativos
        if (clienteRepository.hasEmprestimosAtivos(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente possui empréstimos ativos");
        }

        this.clienteRepository.delete(cliente);
    }

    public void excluirPorUuid(UUID uuid) {
        Cliente cliente = buscarPorUuid(uuid);

        // Verificar se cliente tem empréstimos ativos
        if (clienteRepository.hasEmprestimosAtivosByUuid(uuid)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente possui empréstimos ativos");
        }

        this.clienteRepository.delete(cliente);
    }

    public void atualizar(Long id, Cliente cliente) {
        Cliente c = this.clienteRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Verificar se CPF já existe em outro cliente
        Optional<Cliente> clienteComCpf = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteComCpf.isPresent() && !clienteComCpf.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado em outro cliente");
        }

        c.setNome(cliente.getNome());
        c.setCpf(cliente.getCpf());
        c.setTelefone(cliente.getTelefone());
        c.setEndereco(cliente.getEndereco());

        this.clienteRepository.save(c);
    }

    public void atualizarPorUuid(Cliente cliente) {
        Cliente c = this.clienteRepository.findByUuid(cliente.getUuid())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));

        // Verificar se CPF já existe em outro cliente
        Optional<Cliente> clienteComCpf = clienteRepository.findByCpf(cliente.getCpf());
        if (clienteComCpf.isPresent() && !clienteComCpf.get().getUuid().equals(cliente.getUuid())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "CPF já cadastrado em outro cliente");
        }

        c.setNome(cliente.getNome());
        c.setCpf(cliente.getCpf());
        c.setTelefone(cliente.getTelefone());
        c.setEndereco(cliente.getEndereco());

        this.clienteRepository.save(c);
    }

    public Cliente buscarPorCpf(String cpf) {
        return this.clienteRepository.findByCpf(cpf)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    public Cliente getClienteUUID(String uuid) {
        UUID uuidFormatado = UUID.fromString(uuid);
        return this.clienteRepository.findByUuid(uuidFormatado)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cliente não encontrado"));
    }

    public void deletarUUID(String uuid) {
        UUID uuidFormatado = UUID.fromString(uuid);
        Cliente cliente = buscarPorUuid(uuidFormatado);

        // Verificar se cliente tem empréstimos ativos
        if (clienteRepository.hasEmprestimosAtivosByUuid(uuidFormatado)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cliente possui empréstimos ativos");
        }

        this.clienteRepository.delete(cliente);
    }
}