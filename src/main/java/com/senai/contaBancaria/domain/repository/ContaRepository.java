package com.senai.contaBancaria.domain.repository;

import com.senai.contaBancaria.domain.entity.Conta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ContaRepository extends JpaRepository<Conta, String> {


        List<Conta> findAllByAtivaTrue();
        Optional<Conta> findByNumeroAndAtivaTrue(String numero);

    }
