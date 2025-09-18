package com.senai.conta_bancaria.application.service;


import com.senai.conta_bancaria.application.dto.ClienteResponseDTO;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Entity

public class ClienteService {

    public final ClienteRepository repository;
    public ClienteResponseDTO registrarCliente(ClienteResponseDTO dto){
        var cliente = repository.findByCpf(dto.cpf());
        if(cliente != null){
            throw new RuntimeException("Cliente ja cadastrado");
        }

        return dto;
    }



}
