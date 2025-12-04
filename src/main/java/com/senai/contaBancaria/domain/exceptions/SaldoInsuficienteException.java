package com.senai.contaBancaria.domain.exceptions;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(String operacao) {
        super("Saldo insuficiente para realizar " + operacao);
    }
}
