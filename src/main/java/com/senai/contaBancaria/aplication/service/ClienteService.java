package com.senai.contaBancaria.aplication.service;


import com.senai.contaBancaria.aplication.dto.ClienteRegistroDTO;
import com.senai.contaBancaria.aplication.dto.ClienteResponseDTO;
import com.senai.contaBancaria.domain.entity.Cliente;
import com.senai.contaBancaria.domain.exceptions.ContaMesmoTipoException;
import com.senai.contaBancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.contaBancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClienteService {


    private final ClienteRepository repository;
    private final PasswordEncoder passwordEncoder;

    @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
    public ClienteResponseDTO registrarClienteOuAnexarConta(ClienteRegistroDTO dto){

        var cliente = repository.findByCpfAndAtivoTrue(dto.cpf()).orElseGet(
                () -> repository.save(dto.toEntity())
        );

        var contas = cliente.getContas();
        var novaConta = dto.contaDTO().toEntity(cliente);

        boolean jaTemTipo = contas.stream()
                .anyMatch(c -> c.getClass().equals(novaConta.getClass()) && c.isAtiva());

        if(jaTemTipo)
            throw new ContaMesmoTipoException();

        cliente.getContas().add(novaConta);
        cliente.setSenha(passwordEncoder.encode(dto.senha()));

        return ClienteResponseDTO.fromEntity(repository.save(cliente));

    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<ClienteResponseDTO> listarClientesAtivos() {
        return repository.findAllByAtivoTrue().stream()
                .map(ClienteResponseDTO::fromEntity)
                .toList();
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDTO buscarClienteAtivoPorCpf(String cpf) {
        var cliente = buscarPorCpfClienteAtivo(cpf);
        return ClienteResponseDTO.fromEntity(cliente);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ClienteResponseDTO atualizarCliente(String cpf, ClienteRegistroDTO dto) {
        var cliente = buscarPorCpfClienteAtivo(cpf);

        cliente.setNome(dto.nome());
        cliente.setCpf(dto.cpf());

        return ClienteResponseDTO.fromEntity(repository.save(cliente));
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public void deletarCliente(String cpf) {
        var cliente = buscarPorCpfClienteAtivo(cpf);
        cliente.setAtivo(false);
        cliente.getContas().forEach(
                 conta -> conta.setAtiva(false)
        );
        repository.save(cliente);
    }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    private Cliente buscarPorCpfClienteAtivo(String cpf) {
        var cliente = repository.findByCpfAndAtivoTrue(cpf).orElseThrow(
                () -> new EntidadeNaoEncontradaException("Cliente")
        );
        return cliente;
    }
}
