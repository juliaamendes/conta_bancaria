package com.senai.contaBancaria.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.MappedSuperclass;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;

@Entity
@EqualsAndHashCode(callSuper = true)
@Getter
@Setter
@DiscriminatorValue("POUPANCA")
@SuperBuilder
@NoArgsConstructor
public class ContaPoupanca extends Conta{

    @Column(precision = 19, scale = 2)
    private BigDecimal rendimento;

    @Override
    public String getTipo() {
        return "POUPANCA";
    }

    public void aplicarRendimento() {
        var ganho = getSaldo().multiply(rendimento);
        setSaldo(getSaldo().add(ganho));
    }
}
