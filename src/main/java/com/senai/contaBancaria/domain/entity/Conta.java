package com.senai.contaBancaria.domain.entity;

import com.senai.contaBancaria.domain.exceptions.SaldoInsuficienteException;
import com.senai.contaBancaria.domain.exceptions.TransferenciaParaMesmaContaException;
import com.senai.contaBancaria.domain.exceptions.ValoresNegativosExecption;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;


@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_conta", discriminatorType = DiscriminatorType.STRING, length = 20)
@Table (name = "conta",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_conta_numero", columnNames = "numero"),
        @UniqueConstraint(name = "uk_cliente_tipo", columnNames = {"cliente_id", "tipo_conta"})
    }
)
@SuperBuilder
@NoArgsConstructor
public abstract class Conta{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;// classe repper é o que representa, que são classes que podem ser nulas ou não
    @Column (nullable = false, length = 20)
    private String numero;
    @Column (nullable = false, precision = 20, scale = 2)
    private BigDecimal saldo;
    @Column (nullable = false)
    private boolean ativa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn (name = "cliente_id", foreignKey = @ForeignKey(name="fk_conta_cliente"))
    private Cliente cliente;

    public abstract String  getTipo();

    public void sacar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "Saque");
        if (this.saldo.compareTo(valor) < 0) {
            throw new SaldoInsuficienteException();
        }
        this.saldo = this.saldo.subtract(valor);
    }
    public void depositar(BigDecimal valor) {
        validarValorMaiorQueZero(valor, "Depósito");
        this.saldo = this.saldo.add(valor);
    }

    protected static void validarValorMaiorQueZero(BigDecimal valor, String operacao) {
        if (valor.compareTo(BigDecimal.ZERO) < 0){
            throw new ValoresNegativosExecption(operacao);
        }
    }

    public void tranferir(BigDecimal valor, Conta contaDestino){
        if (this.equals(contaDestino.getId())){
            throw new TransferenciaParaMesmaContaException();
        }
        this.sacar(valor);
        contaDestino.depositar(valor);

    }
}
