package com.senai.contaBancaria.aplication.dto;

public class AuthDTO {

    public record LoginRequest(
            String email,
            String senha
    ) {}
    public record TokenResponse(
            String token
    ) {}
}
