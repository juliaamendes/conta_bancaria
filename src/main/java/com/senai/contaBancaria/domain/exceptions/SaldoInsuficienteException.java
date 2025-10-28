package com.senai.contaBancaria.domain.exceptions;

public class SaldoInsuficienteException extends RuntimeException {
    public SaldoInsuficienteException(){
        super("Saldo insuficiente para a operação.");
    }
}
