package com.example.start_jobs.service;

import com.example.start_jobs.dto.CandidaturaDTO;
import com.example.start_jobs.dto.StatusCandidaturaDTO;
import com.example.start_jobs.dto.VagaDTO;
import com.example.start_jobs.entity.Candidatura;
import com.example.start_jobs.entity.StatusCandidatura;
import com.example.start_jobs.entity.Usuario;
import com.example.start_jobs.entity.Vaga;
import com.example.start_jobs.repository.CandidaturaRepository;
import com.example.start_jobs.repository.StatusCandidaturaRepository;
import com.example.start_jobs.repository.UsuarioRepository;
import com.example.start_jobs.repository.VagaRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CandidaturaServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private CandidaturaRepository candidaturaRepository;

    @Mock
    private StatusCandidaturaRepository statusCandidaturaRepository;

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private CandidaturaService candidaturaService;

    private Usuario usuario;
    private Vaga vaga;
    private VagaDTO vagaDTO;
    private Candidatura candidatura;

    @Before
    public void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setNome("Test User");

        vaga = new Vaga();
        vaga.setUrl("test-url");
        vaga.setTitulo("Test Job");
        vaga.setEmpresa("Test Company");

        vagaDTO = new VagaDTO(vaga);



        candidatura = new Candidatura();
        candidatura.setUsuario(usuario);
        candidatura.setVaga(vaga);

        List<StatusCandidatura> status = new ArrayList<>();
        StatusCandidatura statusCandidatura = new StatusCandidatura();
        statusCandidatura.setCandidatura(candidatura);
        statusCandidatura.setLabel("teste");
        statusCandidatura.setApproved(false);
        statusCandidatura.setRejected(false);
        statusCandidatura.setDataStatus(LocalDateTime.now());
        statusCandidatura.setIdStatus(1);
        candidatura.setStatusCandidatura(status);
    }

    @Test
    public void testCriarCandidatura_Sucesso() {
        CandidaturaDTO candidaturaDTO = new CandidaturaDTO();
        candidaturaDTO.setIdUsuario(1);
        candidaturaDTO.setVaga(vagaDTO);

        StatusCandidaturaDTO statusDTO = new StatusCandidaturaDTO();
        statusDTO.setLabel("Status");
        statusDTO.setApproved(true);
        statusDTO.setRejected(false);
        candidaturaDTO.setStatusCandidatura(Arrays.asList(statusDTO));

        when(usuarioRepository.findById(1)).thenReturn(Optional.of(usuario));
        when(vagaRepository.findVagaByUrl("test-url")).thenReturn(Optional.of(vaga));
        when(candidaturaRepository.save(any(Candidatura.class))).thenReturn(candidatura);
        when(statusCandidaturaRepository.saveAll(anyList())).thenReturn(null);

        Candidatura result = candidaturaService.criarCandidatura(candidaturaDTO);

        verify(candidaturaRepository).save(any(Candidatura.class));
        verify(statusCandidaturaRepository).saveAll(anyList());

        assertNotNull(result);
        assertEquals(usuario, result.getUsuario());
        assertEquals(vaga, result.getVaga());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCriarCandidatura_UsuarioNaoEncontrado() {
        CandidaturaDTO candidaturaDTO = new CandidaturaDTO();
        candidaturaDTO.setIdUsuario(999);
        candidaturaDTO.setVaga(vagaDTO);

        when(usuarioRepository.findById(999)).thenReturn(Optional.empty());

        candidaturaService.criarCandidatura(candidaturaDTO);
    }

    @Test
    public void testListarCandidaturas() {
        when(candidaturaRepository.findAll()).thenReturn(Arrays.asList(candidatura));

        var result = candidaturaService.listarCandidaturas();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(candidatura.getUsuario().getIdUsuario(), result.get(0).getIdUsuario());
}

    @Test
    public void testAtualizarStatus() {
        CandidaturaDTO candidaturaDTO = new CandidaturaDTO();
        StatusCandidaturaDTO statusDTO = new StatusCandidaturaDTO();
        statusDTO.setId(1);
        statusDTO.setLabel("Updated Status");
        statusDTO.setApproved(true);
        candidaturaDTO.setStatusCandidatura(Arrays.asList(statusDTO));

        StatusCandidatura statusCandidatura = new StatusCandidatura();
        statusCandidatura.setIdStatus(1);
        statusCandidatura.setLabel("Update Status");
        statusCandidatura.setApproved(false);
        statusCandidatura.setRejected(true);
        statusCandidatura.setDataStatus(LocalDateTime.now());

        candidatura.setStatusCandidatura(Arrays.asList(statusCandidatura));

        when(candidaturaRepository.findById(1L)).thenReturn(Optional.of(candidatura));
        when(candidaturaRepository.save(any(Candidatura.class))).thenReturn(candidatura);

        CandidaturaDTO result = candidaturaService.atualizarStatus(1L, candidaturaDTO);

        assertNotNull(result);
        assertEquals("Update Status", result.getStatusCandidatura().get(0).getLabel());
    }

    @Test(expected = RuntimeException.class)
    public void testAtualizarStatus_CandidaturaNaoEncontrada() {
        CandidaturaDTO candidaturaDTO = new CandidaturaDTO();
        when(candidaturaRepository.findById(1L)).thenReturn(Optional.empty());

        candidaturaService.atualizarStatus(1L, candidaturaDTO);
    }
}
