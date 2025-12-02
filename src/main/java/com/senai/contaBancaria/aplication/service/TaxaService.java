package com.senai.contaBancaria.aplication.service;

import com.senai.contaBancaria.aplication.dto.TaxaAtualizacaoDto;
import com.senai.contaBancaria.aplication.dto.TaxaRegistroDto;
import com.senai.contaBancaria.aplication.dto.TaxaResponseDto;
import com.senai.contaBancaria.domain.entity.Taxa;
import com.senai.contaBancaria.domain.entity.Conta;
import com.senai.contaBancaria.domain.exceptions.ContaMesmoTipoException;
import com.senai.contaBancaria.domain.exceptions.EntidadeNaoEncontradaException;
import com.senai.contaBancaria.domain.repository.TaxaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
        return TaxaResponseDto.fromEntity(procurarTaxaAtivo(id));
    }

    // UPDATE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public TaxaResponseDto atualizarTaxa(String id, TaxaAtualizacaoDto dto) {
        Taxa taxa = procurarTaxaAtivo(id);

        taxa.setPercentual(dto.percentual());
        taxa.setValorFixo(dto.valorFixo());

        return TaxaResponseDto.fromEntity(repository.save(taxa));
    }

    // DELETE
    @PreAuthorize("hasAnyRole('ADMIN','GERENTE')")
    public void apagarTaxa(String id) {
        Taxa taxa = procurarTaxaAtivo(id);

        taxa.setAtivo(false);

        repository.save(taxa);
    }

    // Mét0do auxiliar para as requisições
    private Taxa procurarTaxaAtivo(String id) {
        return repository
                .findByIdAndAtivoTrue(id)
                .orElseThrow(() -> new EntidadeNaoEncontradaException("taxa"));
    }
}