package com.senai.contaBancaria.domain.exceptions;

public class ValoresNegativosExecption extends RuntimeException {

    public ValoresNegativosExecption(String operacao)
    {
        super("Não é possivel realizar a operação de "
                + operacao +" com valores negativos");
    }
}
