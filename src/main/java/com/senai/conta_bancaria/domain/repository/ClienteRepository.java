package com.senai.conta_bancaria.domain.repository;

import com.senai.conta_bancaria.domain.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Repository

public interface ClienteRepository  extends JpaRepository<Cliente, String> {
    Cliente fingByCpf(String cpf);

    Object findByCpf(String cpf);
}
