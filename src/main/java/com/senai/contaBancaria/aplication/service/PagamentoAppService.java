package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.aplication.dto.ContaResumoDTO;
import com.senai.contaBancaria.aplication.dto.PagamentoRegistroDto;
import com.senai.contaBancaria.aplication.dto.PagamentoResponseDto;
import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.entity.Pagamento;
import com.senai.contaBancaria.domain.repository.ContaRepository;
import com.senai.contaBancaria.domain.repository.PagamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoAppService {
    private final PagamentoRepository pagamentoRepository;
    private final ContaRepository contaRepository;
    private final ContaService contaService;
    private final PagamentoDomainService pagamentoDomainService;

    @PreAuthorize("hasRole('CLIENTE')")
    public ContaResumoDTO pagar(Long numero, PagamentoRegistroDto dto) {
        Conta conta = contaService.procurarContaAtiva(numero);

        BigDecimal valorDasTaxas = pagamentoDomainService.calcularTaxas(dto.formaPagamento(), dto.valorServico());
        conta.setSaldo(conta.getSaldo().subtract(dto.valorServico().add(valorDasTaxas)));

        return ContaResumoDto.fromEntity(contaRepository.save(conta));

        Pagamento pagamento = PagamentoRegistroDto.toEntity();
        return PagamentoResponseDto.fromEntity(pagamentoRepository.save(pagamento));
    }
}