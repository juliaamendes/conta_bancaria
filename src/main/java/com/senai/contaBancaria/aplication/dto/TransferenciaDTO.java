package com.senai.contaBancaria.aplication.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferenciaDTO(
        @NotNull
        BigDecimal valor,
        @NotBlank
        String contaDestino
) {
}
