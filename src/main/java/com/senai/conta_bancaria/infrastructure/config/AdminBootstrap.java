package com.senai.conta_bancaria.infrastructure.config;

import com.senai.conta_bancaria.domain.entity.Cliente;
import com.senai.conta_bancaria.domain.repository.ClienteRepository;
import com.senai.conta_bancaria.domain.repository.ContaRepository;
import com.senai.modelo_autenticacao_autorizacao.domain.entity.Professor;
import com.senai.modelo_autenticacao_autorizacao.domain.enums.Role;
import com.senai.modelo_autenticacao_autorizacao.domain.repository.ProfessorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.events.Event;

@Component
@RequiredArgsConstructor
public class AdminBootstrap implements CommandLineRunner {

    private final ContaRepository ContaRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${sistema.admin.email}")
    private String adminEmail;

    @Value("${sistema.admin.senha}")
    private String adminSenha;

    @Override
    public void run(String... args) {
        ContaRepository.findAllByAtivaTrue().iterator(
                prof -> {
                    if (!conta.isAtivo()) {
                        prof.setAtivo(true);
                        ContaRepository.save(conta);
                    }
                },
                () -> {
                     conta = Cliente.builder()
                            .nome("Administrador Provisório")
                            .email(adminEmail)
                            .cpf("000.000.000-00")
                            .senha(passwordEncoder.encode(adminSenha))
                            .role(Role.ADMIN)
                            .build();
                    ClienteRepository.save(conta);
                    System.out.println("⚡ Usuário admin provisório criado: " + adminEmail);
                }
        );
    }
}
