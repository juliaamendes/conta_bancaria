package com.senai.contaBancaria.aplication.dto;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ValorSaqueDepositoDTO(
        @NotNull
        BigDecimal valor

) {
}
