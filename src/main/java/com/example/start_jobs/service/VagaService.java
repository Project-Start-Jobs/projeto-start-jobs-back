package com.example.start_jobs.service;

import com.example.start_jobs.entity.Dicas;
import com.example.start_jobs.entity.Vaga;
import com.example.start_jobs.repository.VagaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class VagaService {
    @Autowired
    private VagaRepository vagaRepository;

    @Transactional
    public List<Vaga> listarTodasAsVagas() {
        return vagaRepository.findAll();
    }

    @Transactional
    public Vaga criarVaga(Vaga vaga) {
        return vagaRepository.save(vaga);
    }

    @Transactional
    public Vaga atualizarVaga(Long id, Vaga vagaAtualizada) {
        Vaga vaga = vagaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaga não encontrada"));

        vaga.setTitulo(vagaAtualizada.getTitulo());
        vaga.setDescricao(vagaAtualizada.getDescricao());
        vaga.setEmpresa(vagaAtualizada.getEmpresa());
        vaga.setLocalizacao(vagaAtualizada.getLocalizacao());
        vaga.setUrl(vagaAtualizada.getUrl());
        return vagaRepository.save(vaga);
    }

    @Transactional
    public Optional<Vaga> buscarVagaPorId(Long id) {
        return vagaRepository.findById(id);
    }

    @Transactional
    public void deletarVaga(Long id) {
        vagaRepository.deleteById(id);
    }
}
