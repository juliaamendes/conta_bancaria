package com.senai.contaBancaria.domain.exceptions;

public class ContaMesmoTipoException extends RuntimeException {
    public ContaMesmoTipoException() {
        super("O cliente ja possui uma conta deste tipo.");
    }
}
