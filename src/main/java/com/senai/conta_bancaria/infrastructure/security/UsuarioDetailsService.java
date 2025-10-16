package com.senai.conta_bancaria.infrastructure.security;

import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioDetailsService implements UserDetailsService {

    private final ContaRepository repository;

    @Override
    public  loadUserByUsername(String email) throws UsernameNotFoundException {
        var gerente = repository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));

        return new User(
                gerente.getEmail(),
                gerente.getSenha(),
                List.of(new SimpleGrantedAuthority("ROLE_" + gerente.getRole().name()))
        );
    }
}
