package com.senai.contaBancaria.aplication.dto;

import com.senai.contaBancaria.domain.entity.Cliente;
import com.senai.contaBancaria.domain.entity.Pagamento;
import com.senai.contaBancaria.domain.entity.Taxa;
import com.senai.contaBancaria.domain.enums.FormaPagamento;
import com.senai.contaBancaria.domain.enums.StatusPagamento;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

public record PagamentoRegistroDto(
        @NotNull(message = "O ID da conta não pode ser nulo.")
        String contaId,

        @NotNull(message = "O nome do serviço pago não pode ser nulo.")
        @NotBlank(message = "O nome do serviço pago não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do serviço pago deve ter entre 3 e 100 caracteres.")
        String servico,

        @NotNull(message = "O valor do serviço pago não pode ser nulo.")
        @Positive(message = "O valor do serviço pago deve ser maior que zero.")
        BigDecimal valorServico,

        List<TaxaRegistroDto> taxas,

        @NotNull(message = "A forma de pagamento não pode ser nula.")
        @NotBlank(message = "A forma de pagamento não pode ser vazia.")
        @Size(min = 3, max = 100, message = "A forma de pagamento deve ter entre 3 e 100 caracteres.")
        String formaPagamento
) {
        public Pagamento toEntity() {
                return Pagamento.builder()
                        .conta() // TODO
                        .servico(servico)
                        .
        }

        private Cliente conta;

        private String servico;

        private BigDecimal valorPago;

        private LocalDateTime dataPagamento;

        protected StatusPagamento status;

        private HashSet<Taxa> taxas;

        public FormaPagamento formaPagamento;
}