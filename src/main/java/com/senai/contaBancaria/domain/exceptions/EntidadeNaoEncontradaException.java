package com.senai.contaBancaria.domain.exceptions;

public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException(String entidade) {
        super( entidade + " não existente inativa");
    }
}
