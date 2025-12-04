package com.senai.contaBancaria.domain.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Entity
@Data
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
    private List<Conta> contas;

    @Column (nullable = false)
    private boolean ativo;


}
