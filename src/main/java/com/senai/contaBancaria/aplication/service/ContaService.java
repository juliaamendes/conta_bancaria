package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.aplication.dto.ContaAtualizacaoDTO;
import com.senai.contaBancaria.aplication.dto.ContaResumoDTO;
import com.senai.contaBancaria.aplication.dto.TransferenciaDTO;
import com.senai.contaBancaria.aplication.dto.ValorSaqueDepositoDTO;
import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.entity.ContaCorrente;
import com.senai.contaBancaria.domain.entity.ContaPoupanca;
import com.senai.contaBancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.contaBancaria.domain.exceptions.RendimentoInvalidoException;
import com.senai.contaBancaria.domain.exceptions.TipoDeContaInvalidaException;
import com.senai.contaBancaria.domain.repository.ContaRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ContaService {
    private final ContaRepository repository;

    // CREATE: embutido em Cliente

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ContaResumoDTO> listarTodasAsContas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(ContaResumoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public List<ContaResumoDTO> listarContasPorCpf(Long cpf) {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .filter(c -> c.getCliente().getCpf().equals(cpf))
                .map(ContaResumoDTO::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO buscarConta(Long numero) {
        return ContaResumoDTO.fromEntity(procurarContaAtiva(numero));
    }

    // UPDATE
    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO atualizarConta(Long numero, ContaAtualizacaoDTO dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.setSaldo(dto.saldo());
        if (conta instanceof ContaCorrente contaCorrente) {
            contaCorrente.setLimite(dto.limite());
            contaCorrente.setTaxa(dto.taxa());
        } else if (conta instanceof ContaPoupanca contaPoupanca) {
            contaPoupanca.setRendimento(dto.rendimento());
        } else {
            throw new TipoDeContaInvalidaException("");
        }

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public void apagarConta(Long numero) {
        Conta conta = procurarContaAtiva(numero);

        conta.setAtivo(false);

        repository.save(conta);
    }



    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO sacar(Long numero, @Valid ValorSaqueDepositoDTO dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.sacar(dto.valor());

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO depositar(Long numero, @Valid ValorSaqueDepositoDTO dto) {
        Conta conta = procurarContaAtiva(numero);

        conta.depositar(dto.valor());

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO transferir(Long numeroOrigem, @Valid TransferenciaDTO dto) {
        Conta contaOrigem = procurarContaAtiva(numeroOrigem);
        Conta contaDestino = procurarContaAtiva(dto.numeroDestino());

        contaOrigem.transferir(contaDestino, dto.valor());

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO rendimento(Long numero) {
        Conta conta = procurarContaAtiva(numero);

        if (!(conta instanceof ContaPoupanca contaPoupanca))
            throw new RendimentoInvalidoException();

        contaPoupanca.aplicarRendimento();

        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    // Mét0do auxiliar para as requisições
    protected  Conta procurarContaAtiva(Long numero) {
        return repository
                .findByNumeroAndAtivoTrue(numero)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("conta"));
    }
}