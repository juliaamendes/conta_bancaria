package com.senai.conta_bancaria.domain.exception;

public class ValoresNegativosException extends RuntimeException {
    public ValoresNegativosException(String operacao) {
        super("Não é possível realizar a operação de "
                + operacao + " com valores negativos.");
    }
}
