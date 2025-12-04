package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.aplication.dto.PagamentoRegistroDto;
import com.senai.contaBancaria.aplication.dto.PagamentoResponseDto;
import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.entity.Pagamento;
import com.senai.contaBancaria.domain.entity.Taxa;
import com.senai.contaBancaria.domain.enums.FormaPagamento;
import com.senai.contaBancaria.domain.exceptions.FormaDePagamentoInvalidaException;
import com.senai.contaBancaria.domain.exceptions.SaldoInsuficienteException;
import com.senai.contaBancaria.domain.repository.PagamentoRepository;
import com.senai.contaBancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoAppService {
    private final PagamentoRepository pagamentoRepository;

    private final TaxaService taxaService;
    private final ContaService contaService;
    private final PagamentoDomainService pagamentoDomainService;

    @PreAuthorize("hasRole('CLIENTE')")
    public PagamentoResponseDto pagar(Long numero, PagamentoRegistroDto dto) {
        FormaPagamento formaPagamento = pagamentoDomainService.validarFormaPagamento(dto.formaPagamento());

        Set<Taxa> taxas = taxaService.procurarTaxasPorFormaPagamento(formaPagamento);
        Conta conta = contaService.procurarContaAtiva(numero);

        Pagamento pagamento = dto.toEntity(conta, taxas);

        BigDecimal valorTaxa = pagamentoDomainService.calcularTaxa(dto.valorPago(), taxas);
        BigDecimal valorTotal = pagamentoDomainService.validarSaldo(numero, dto.valorPago(), valorTaxa);

        conta.setSaldo(conta.getSaldo().subtract(valorTotal));
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento), valorTaxa);
    }
}