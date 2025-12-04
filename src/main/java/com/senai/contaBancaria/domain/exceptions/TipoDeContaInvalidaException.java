package com.senai.contaBancaria.domain.exceptions;

public class TipoDeContaInvalidaException extends RuntimeException {
    public TipoDeContaInvalidaException(String tipo) {

        super("Tipo de conta inválida. Os tipos válidos são: 'corrente' ou 'poupanca'.");
    }
}
