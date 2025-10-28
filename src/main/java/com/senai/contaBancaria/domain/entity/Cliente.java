package com.senai.contaBancaria.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@Table(
        name = "cliente",
        uniqueConstraints = @UniqueConstraint(name="uk_cliente_cpf", columnNames = "cpf")
)

public class Cliente extends Usuario {

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    //cascade para criar o relacionamnto entre cliente e conta, criando simultaneamente. Além de, quando apagar o cliente, apagar todas as contas relacionadas.
    private List<Conta> contas;

    @Column (nullable = false)
    private boolean ativo;//indica se a conta/cliente está ativo ou inativo

}
