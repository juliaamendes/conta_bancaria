package com.senai.contaBancaria.domain.repository;

import com.senai.contaBancaria.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, String> {
    Optional<Cliente> findByCpfAndAtivoTrue (String cpf);

    List<Cliente> findAllByAtivoTrue();
}