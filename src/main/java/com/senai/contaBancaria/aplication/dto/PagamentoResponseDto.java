package com.senai.contaBancaria.aplication.dto;

import com.senai.contaBancaria.domain.entity.Pagamento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PagamentoResponseDto(
        String id,
        String servico,
        BigDecimal valorPago,
        LocalDateTime dataPagamento,
        String status,
        List<TaxaResponseDto> taxas,
        String formaPagamento
) {
        public static PagamentoResponseDto fromEntity(Pagamento pagamento) {
                List<TaxaResponseDto> taxas = pagamento
                        .getTaxas()
                        .stream()
                        .map(TaxaResponseDto::fromEntity)
                        .toList();
                return new PagamentoResponseDto(
                        pagamento.getId(),
                        pagamento.getServico(),
                        pagamento.getValorPago(),
                        pagamento.getDataPagamento(),
                        pagamento.getStatus().name(),
                        taxas,
                        pagamento.getFormaPagamento().name()
                );
        }
}