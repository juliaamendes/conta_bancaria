package com.senai.contaBancaria.aplication.service;


import com.senai.contaBancaria.Infrastructure.Security.JwtService;
import com.senai.contaBancaria.aplication.dto.AuthDTO;
import com.senai.contaBancaria.domain.entity.Usuario;
import com.senai.contaBancaria.domain.exceptions.UsuarioNaoEncontradoException;
import com.senai.contaBancaria.domain.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarios;
    private final PasswordEncoder encoder;
    private final JwtService jwt;

    public String login(AuthDTO.LoginRequest req) {
        Usuario usuario = usuarios.findByEmail(req.email())
                .orElseThrow(() ->  new UsuarioNaoEncontradoException("Usuário não encontrado"));

        if (!encoder.matches(req.senha(), usuario.getSenha())) {
            throw new BadCredentialsException("Credenciais inválidas");
        }

        return jwt.generateToken(usuario.getEmail(), usuario.getRole().name());
    }
}
