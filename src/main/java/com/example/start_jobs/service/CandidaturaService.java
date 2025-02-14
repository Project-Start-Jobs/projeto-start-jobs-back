package com.example.start_jobs.service;

import com.example.start_jobs.dto.CandidaturaDTO;
import com.example.start_jobs.dto.StatusCandidaturaDTO;
import com.example.start_jobs.entity.Candidatura;
import com.example.start_jobs.entity.StatusCandidatura;
import com.example.start_jobs.entity.Usuario;
import com.example.start_jobs.entity.Vaga;
import com.example.start_jobs.repository.CandidaturaRepository;
import com.example.start_jobs.repository.StatusCandidaturaRepository;
import com.example.start_jobs.repository.UsuarioRepository;
import com.example.start_jobs.repository.VagaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CandidaturaService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CandidaturaRepository candidaturaRepository;

    @Autowired
    private StatusCandidaturaRepository statusCandidaturaRepository;

    @Autowired
    private VagaRepository vagaRepository;

    public Candidatura criarCandidatura(CandidaturaDTO candidaturaDTO) {
        Optional<Usuario> usuarioOptional = usuarioRepository.findById(candidaturaDTO.getIdUsuario());

        if (usuarioOptional.isEmpty()) {
            throw new IllegalArgumentException("Usuário não encontrado");
        }

        Usuario usuario = usuarioOptional.get();

        Optional<Candidatura> candidaturaExistente = candidaturaRepository
                .findByUsuarioIdUsuarioAndAndVaga_Url(usuario.getIdUsuario(), candidaturaDTO.getVaga().getUrl());

        if (candidaturaExistente.isPresent()) {
            throw new IllegalArgumentException("Você já se candidatou nessa vaga!");
        }

        Vaga vaga = vagaRepository.findVagaByUrl(candidaturaDTO.getVaga().getUrl())
                .orElseGet(() -> {
                    Vaga novaVaga = new Vaga();
                    novaVaga.setTitulo(candidaturaDTO.getVaga().getTitulo());
                    novaVaga.setDescricao(candidaturaDTO.getVaga().getDescricao());
                    novaVaga.setEmpresa(candidaturaDTO.getVaga().getEmpresa());
                    novaVaga.setLocalizacao(candidaturaDTO.getVaga().getLocalizacao());
                    novaVaga.setSenioridade(candidaturaDTO.getVaga().getSenioridade());
                    novaVaga.setModeloTrabalho(candidaturaDTO.getVaga().getModeloTrabalho());
                    novaVaga.setUrl(candidaturaDTO.getVaga().getUrl());
                    novaVaga.setDataCriacao(candidaturaDTO.getVaga().getDataCriacao());
                    return vagaRepository.save(novaVaga);
                });

        Candidatura candidatura = new Candidatura();
        candidatura.setUsuario(usuario);
        candidatura.setVaga(vaga);

        Candidatura candidaturaSalva = candidaturaRepository.save(candidatura);

        List<StatusCandidatura> statusCandidaturasList = new ArrayList<>();
        for (StatusCandidaturaDTO statusDTO : candidaturaDTO.getStatusCandidatura()) {
            StatusCandidatura statusCandidatura = new StatusCandidatura();
            statusCandidatura.setLabel(statusDTO.getLabel());
            statusCandidatura.setApproved(statusDTO.isApproved());
            statusCandidatura.setRejected(statusDTO.isRejected());
            statusCandidatura.setDataStatus(LocalDateTime.now());
            statusCandidatura.setCandidatura(candidaturaSalva);

            statusCandidaturasList.add(statusCandidatura);
        }

        statusCandidaturaRepository.saveAll(statusCandidaturasList);

        return candidaturaSalva;
    }



    public List<CandidaturaDTO> listarCandidaturas() {
        List<Candidatura> candidatura = candidaturaRepository.findAll();
        return candidatura.stream()
                .map(CandidaturaDTO::new)
                .collect(Collectors.toList());
    }

    public Optional<Candidatura> buscarCandidaturaPorId(Long id) {
        return candidaturaRepository.findById(id);
    }

    public List<CandidaturaDTO> listarCandidaturasPorUsuario(Long idUsuario) {
        List<Candidatura> candidaturas = candidaturaRepository.findByUsuarioIdUsuario(idUsuario);


        return candidaturas.stream()
                .peek(c -> c.getStatusCandidatura().sort(Comparator.comparing(StatusCandidatura::getDataStatus)))
                .map(CandidaturaDTO::new)
                .collect(Collectors.toList());
    }


    public Candidatura atualizarCandidatura(Long id, Candidatura candidaturaAtualizada) {
        Candidatura candidatura = candidaturaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidatura não encontrada"));

        candidatura.setUsuario(candidaturaAtualizada.getUsuario());
        candidatura.setVaga(candidaturaAtualizada.getVaga());
        return candidaturaRepository.save(candidatura);
    }

    @Transactional
    public CandidaturaDTO atualizarStatus(Long id, CandidaturaDTO candidaturaDTO) {
        Optional<Candidatura> candidaturaOptional = candidaturaRepository.findById(id);

        if (candidaturaOptional.isPresent()) {
            Candidatura candidatura = candidaturaOptional.get();

            for (StatusCandidatura status : candidatura.getStatusCandidatura()) {
                StatusCandidaturaDTO statusDTO = candidaturaDTO.getStatusCandidatura().stream()
                        .filter(dto -> dto.getId().equals(status.getIdStatus()))
                        .findFirst()
                        .orElse(null);

                if (statusDTO != null) {
                    status.setApproved(statusDTO.isApproved());
                    status.setRejected(statusDTO.isRejected());
                    if (status.getDataStatus() == null) {
                        status.setDataStatus(LocalDateTime.now());
                    }
                }
            }
            List<StatusCandidatura> sortedStatus = candidatura.getStatusCandidatura().stream()
                    .sorted(Comparator.comparing(StatusCandidatura::getDataStatus))
                    .collect(Collectors.toList());
            candidatura.setStatusCandidatura(sortedStatus);

            candidaturaRepository.save(candidatura);

            return new CandidaturaDTO(candidatura);
        } else {
            throw new RuntimeException("Candidatura não encontrada");
        }
    }


    @Transactional
    public CandidaturaDTO adicionarNovosStatus(Long id, List<StatusCandidaturaDTO> statusCandidaturaDTOList) {
        Optional<Candidatura> candidaturaOptional = candidaturaRepository.findById(id);

        if (candidaturaOptional.isPresent()) {
            Candidatura candidatura = candidaturaOptional.get();

            List<StatusCandidatura> newStatuses = statusCandidaturaDTOList.stream()
                    .map(statusDTO -> {
                        StatusCandidatura status = new StatusCandidatura(statusDTO, candidatura);

                        if (status.getDataStatus() == null) {
                            status.setDataStatus(LocalDateTime.now());
                        }

                        status.setCandidatura(candidatura);
                        return status;
                    })
                    .toList();

            candidatura.getStatusCandidatura().addAll(newStatuses);

            candidaturaRepository.save(candidatura);

            return new CandidaturaDTO(candidatura);
        } else {
            throw new RuntimeException("Candidatura não encontrada");
        }
    }

    public void deletarCandidatura(Long id) {
        candidaturaRepository.deleteById(id);
    }
}
