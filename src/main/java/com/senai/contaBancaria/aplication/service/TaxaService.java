package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.aplication.dto.TaxaAtualizacaoDto;
import com.senai.contaBancaria.aplication.dto.TaxaRegistroDto;
import com.senai.contaBancaria.aplication.dto.TaxaResponseDto;
import com.senai.contaBancaria.domain.entity.Taxa;
import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.enums.FormaPagamento;
import com.senai.contaBancaria.domain.exceptions.ContaMesmoTipoException;
import com.senai.contaBancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.contaBancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxaService {
    private final TaxaRepository repository;

    // CREATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public TaxaResponseDto registrarTaxa(TaxaRegistroDto dto) {
        Taxa taxaRegistrada = repository // verifica se a taxa já existe
                .findByDescricaoAndAtivoTrue(dto.descricao())
                .orElseGet( // se não existir, cria um novo
                        () -> repository.save(dto.toEntity())
                );

        return TaxaResponseDto.fromEntity(repository.save(taxaRegistrada));
    }

    // READ
    @Transactional(readOnly = true)
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public List<TaxaResponseDto> listarTodasAsTaxas() {
        return repository
                .findAllByAtivoTrue()
                .stream()
                .map(TaxaResponseDto::fromEntity)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaxaResponseDto buscarTaxa(String id) {
        return TaxaResponseDto.fromEntity(procurarTaxaAtiva(id));
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public TaxaResponseDto atualizarTaxa(String id, TaxaAtualizacaoDto dto) {
        Taxa taxa = procurarTaxaAtiva(id);

        taxa.setDescricao(dto.descricao());
        taxa.setPercentual(dto.percentual());
        taxa.setValorFixo(dto.valorFixo());
        taxa.setDescricao(dto.formaPagamento());

        return TaxaResponseDto.fromEntity(repository.save(taxa));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public void apagarTaxa(String id) {
        Taxa taxa = procurarTaxaAtiva(id);

        taxa.setAtivo(false);

        repository.save(taxa);
    }

    // Mét0dos auxiliares para as requisições
    protected Taxa procurarTaxaAtiva(String id) {
        return repository
                .findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("taxa"));
    }

    protected Set<Taxa> procurarTaxasPorFormaPagamento(FormaPagamento formaPagamento) {
        return (Set<Taxa>) repository
                .findAllByFormaPagamentoAndAtivoTrue(formaPagamento);
    }
}