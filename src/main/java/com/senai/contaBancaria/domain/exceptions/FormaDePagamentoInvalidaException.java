package com.senai.contaBancaria.domain.exceptions;

import com.senai.contaBancaria.domain.enums.FormaPagamento;

public class FormaDePagamentoInvalidaException extends RuntimeException {
    public FormaDePagamentoInvalidaException(String tipo) {
        super(buildMensagem(tipo));
    }

    private static String buildMensagem(String tipo) {
        StringBuilder tiposValidos = new StringBuilder();
        for (FormaPagamento formaPagamento : FormaPagamento.values()) {
            tiposValidos.append(formaPagamento.name()).append("; ");
        }
        return "Forma de pagamento " + tipo + " não foi encontrada. Tipos válidos: " + tiposValidos;
    }
}