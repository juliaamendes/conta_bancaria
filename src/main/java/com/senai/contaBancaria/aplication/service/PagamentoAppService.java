package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.aplication.dto.ContaResumoDTO;
import com.senai.contaBancaria.aplication.dto.PagamentoRegistroDto;
import com.senai.contaBancaria.aplication.dto.PagamentoResponseDto;
import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.entity.Pagamento;
import com.senai.contaBancaria.domain.entity.Taxa;
import com.senai.contaBancaria.domain.enums.FormaPagamento;
import com.senai.contaBancaria.domain.repository.ContaRepository;
import com.senai.contaBancaria.domain.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

        BigDecimal taxas = (BigDecimal) taxaService.procurarTaxasPorFormaPagamento(formaPagamento);
        Conta conta = contaService.procurarContaAtiva(numero);

        Pagamento pagamento = dto.toEntity(conta, (Set<Taxa>) taxas);

        BigDecimal valorTaxa = pagamentoDomainService.calcularTaxa(dto.valorPago(), taxas);
        BigDecimal valorTotal = pagamentoDomainService.validarSaldo(numero, dto.valorPago(), valorTaxa);

        conta.setSaldo(conta.getSaldo().subtract(valorTotal));
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento), valorTaxa);
    }
}