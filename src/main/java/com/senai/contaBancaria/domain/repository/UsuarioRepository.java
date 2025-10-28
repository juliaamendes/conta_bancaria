package com.senai.contaBancaria.domain.repository;

import com.senai.contaBancaria.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.Optional;

public interface UsuarioRepository extends JpaRepository <Usuario, String> {
    Optional <Usuario> findByEmail(String email);
}
