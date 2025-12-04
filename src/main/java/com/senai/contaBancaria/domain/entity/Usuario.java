package com.senai.contaBancaria.domain.entity;

import com.senai.contaBancaria.domain.enums.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Data
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Inheritance(strategy = InheritanceType.JOINED)
@Table(
        name = "usuarios",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "cpf")
        }
)
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false, length = 120)
    private String nome;

    @Column(nullable = false, length = 11)
    private Long cpf;

    @Column(nullable = false, length = 120)
    private String email;

    @Column(nullable = false, length = 120)
    private String senha;

    @Column(nullable = false)
    private boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected Role role;
}
