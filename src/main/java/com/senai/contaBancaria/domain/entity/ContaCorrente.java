package com.senai.contaBancaria.domain.entity;

import com.senai.contaBancaria.domain.exceptions.SaldoInsuficienteException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@EqualsAndHashCode(callSuper = true)
@DiscriminatorValue("CORRENTE")
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class ContaCorrente extends Conta {
    @Column(precision = 19, scale = 2)
    private BigDecimal limite;
    @Column(precision = 19, scale = 2)
    private BigDecimal taxa;

    @Override
    public String getTipo() {
        return "CORRENTE";
    }

    @Override
    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "Saque");


        BigDecimal custoSaque = valor.multiply(taxa);
        BigDecimal totalSaque = valor.add(custoSaque);

        if (getSaldo().add(limite).compareTo(totalSaque) < 0){
            throw new SaldoInsuficienteException();
    }
        this.setSaldo(this.getSaldo().subtract(totalSaque));
    }


}