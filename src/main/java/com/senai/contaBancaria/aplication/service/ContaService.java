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
import com.senai.contaBancaria.domain.repository.ContaRepository;
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


        @Transactional(readOnly = true)
        @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
        public List<ContaResumoDTO> listarTodasContas() {
            return repository.findAllByAtivaTrue().stream()
                    .map(ContaResumoDTO::fromEntity).toList();
        }

        @Transactional(readOnly = true)
        @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
        public ContaResumoDTO buscarContaPorNumero(String numero) {
            return ContaResumoDTO.fromEntity(
                    repository.findByNumeroAndAtivaTrue(numero)
                            .orElseThrow(() -> new EntidadeNaoEncontradaException("Conta"))
            );
        }

    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ContaResumoDTO atualizarConta(String numeroConta, ContaAtualizacaoDTO dto) {
        var conta = buscarContaAtivoPorNumero(numeroConta);

        if(conta instanceof ContaPoupanca poupanca){
            poupanca.setRendimento(dto.rendimento());
        } else if (conta instanceof ContaCorrente corrente) {
            corrente.setLimite(dto.limite());
            corrente.setTaxa(dto.taxa());
        }
        conta.setSaldo(dto.saldo());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    public void deletarConta(String numeroDaConta) {
        var conta = buscarContaAtivoPorNumero(numeroDaConta);
        conta.setAtiva(false);
        repository.save(conta);
    }

    @PreAuthorize("hasAnyRole('ADMIN','CLIENTE')")
    private Conta buscarContaAtivoPorNumero(String numeroDaConta) {
        var conta = repository.findByNumeroAndAtivaTrue(numeroDaConta).orElseThrow(
                ()-> new EntidadeNaoEncontradaException("Conta")
        );
        return conta;
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO sacar(String numeroDaConta, ValorSaqueDepositoDTO dto) {
        var conta = buscarContaAtivoPorNumero(numeroDaConta);
        conta.sacar(dto.valor());
        return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO depositar(String numeroDaConta, ValorSaqueDepositoDTO dto) {
            var conta = buscarContaAtivoPorNumero(numeroDaConta);
            conta.depositar(dto.valor());
            return ContaResumoDTO.fromEntity(repository.save(conta));
    }

    @PreAuthorize("hasAnyRole('CLIENTE')")
    public ContaResumoDTO transferir(String numeroDaConta,  TransferenciaDTO dto) {
        var contaOrigem = buscarContaAtivoPorNumero(numeroDaConta);
        var contaDestino = buscarContaAtivoPorNumero(dto.contaDestino());

        contaOrigem.sacar(dto.valor());
        contaDestino.depositar(dto.valor());

        repository.save(contaDestino);
        return ContaResumoDTO.fromEntity(repository.save(contaOrigem));
    }


    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public ContaResumoDTO aplicarRendimento(String numeroDaConta) {
        var conta = buscarContaAtivoPorNumero(numeroDaConta);
        if(conta instanceof ContaPoupanca poupanca){
            poupanca.aplicarRendimento();
            return ContaResumoDTO.fromEntity(repository.save(conta));
        }
        throw new RendimentoInvalidoException();
    }
}
