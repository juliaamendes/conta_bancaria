package com.senai.contaBancaria.domain.repository;

import com.senai.contaBancaria.domain.entity.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository<Pagamento, String> {
}