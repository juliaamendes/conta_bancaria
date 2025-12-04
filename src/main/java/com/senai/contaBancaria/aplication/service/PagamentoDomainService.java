package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.entity.Taxa;
import com.senai.contaBancaria.domain.enums.FormaPagamento;
import com.senai.contaBancaria.domain.exceptions.FormaDePagamentoInvalidaException;
import com.senai.contaBancaria.domain.exceptions.SaldoInsuficienteException;
import com.senai.contaBancaria.domain.repository.TaxaRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PagamentoDomainService {
    private final TaxaRepository repository;

    @PreAuthorize("hasRole('CLIENTE')")
    public FormaPagamento validarFormaPagamento(String formaPagamento) {
        boolean formaPagamentoExiste = false;
        for (FormaPagamento formaPossivel : FormaPagamento.values()) {
            if (formaPagamento.equals(formaPossivel.name())) {
                formaPagamentoExiste = true;
                break;
            }
        }
        if (!formaPagamentoExiste) throw new FormaDePagamentoInvalidaException(formaPagamento);
        return FormaPagamento.valueOf(formaPagamento);
    }

    @PreAuthorize("hasRole('CLIENTE')")
    public BigDecimal calcularTaxa(@NotNull(message = "O valor do serviço pago não pode ser nulo.") @Positive(message = "O valor do serviço pago deve ser maior que zero.") BigDecimal formaPagamento, BigDecimal valorServico) {
        List<Taxa> taxas = repository.findAllByFormaPagamentoAndAtivoTrue(formaPagamento);

        BigDecimal valorDasTaxas = BigDecimal.valueOf(0);
        for (Taxa taxa : taxas) {
            valorDasTaxas = valorDasTaxas.add(taxa
                    .getPercentual()
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                    .multiply(valorServico)
            );
        }

        return valorDasTaxas;
    }
}
    @PreAuthorize("hasRole('CLIENTE')")
    public BigDecimal validarSaldo(Long numero, BigDecimal valorPago, BigDecimal valorTaxa) {
        Conta conta = contaService.procurarContaAtiva(numero);

        BigDecimal valorTotal = valorPago.add(valorTaxa);


        if(conta.getSaldo().compareTo(valorTotal) < 0) throw new SaldoInsuficienteException("pagamento");

        return valorTotal;
    }


