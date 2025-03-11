package com.example.start_jobs.service;

import com.example.start_jobs.entity.Vaga;
import com.example.start_jobs.repository.VagaRepository;
import com.example.start_jobs.service.VagaService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class VagaServiceTest {

    @Mock
    private VagaRepository vagaRepository;

    @InjectMocks
    private VagaService vagaService;

    private Vaga vaga;

    @Before
    public void setUp() {
        vaga = new Vaga();
        vaga.setIdVaga(1);
        vaga.setTitulo("Desenvolvedor Java");
        vaga.setDescricao("Vaga para desenvolvedor Java com experiência");
        vaga.setEmpresa("Tech Company");
        vaga.setLocalizacao("São Paulo");
        vaga.setUrl("https://techcompany.com/vagas/desenvolvedor-java");
    }

    @Test
    public void testListarTodasAsVagas() {
        when(vagaRepository.findAll()).thenReturn(Arrays.asList(vaga));

        var vagas = vagaService.listarTodasAsVagas();

        assertNotNull(vagas);
        assertEquals(1, vagas.size());
        assertEquals(vaga.getTitulo(), vagas.get(0).getTitulo());
    }

    @Test
    public void testCriarVaga() {
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vaga);

        Vaga result = vagaService.criarVaga(vaga);

        verify(vagaRepository).save(any(Vaga.class));

        assertNotNull(result);
        assertEquals(vaga.getTitulo(), result.getTitulo());
    }

    @Test
    public void testAtualizarVaga() {
        Vaga vagaAtualizada = new Vaga();
        vagaAtualizada.setTitulo("Desenvolvedor Python");
        vagaAtualizada.setDescricao("Vaga para desenvolvedor Python com experiência");
        vagaAtualizada.setEmpresa("Tech Company");
        vagaAtualizada.setLocalizacao("São Paulo");
        vagaAtualizada.setUrl("https://techcompany.com/vagas/desenvolvedor-python");

        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));
        when(vagaRepository.save(any(Vaga.class))).thenReturn(vagaAtualizada);

        Vaga result = vagaService.atualizarVaga(1L, vagaAtualizada);

        verify(vagaRepository).save(any(Vaga.class));

        assertNotNull(result);
        assertEquals(vagaAtualizada.getTitulo(), result.getTitulo());
        assertEquals(vagaAtualizada.getDescricao(), result.getDescricao());
    }

    @Test(expected = RuntimeException.class)
    public void testAtualizarVaga_VagaNaoEncontrada() {
        when(vagaRepository.findById(1L)).thenReturn(Optional.empty());

        vagaService.atualizarVaga(1L, vaga);
    }

    @Test
    public void testBuscarVagaPorId() {
        when(vagaRepository.findById(1L)).thenReturn(Optional.of(vaga));

        Optional<Vaga> result = vagaService.buscarVagaPorId(1L);

        assertTrue(result.isPresent());
        assertEquals(vaga.getTitulo(), result.get().getTitulo());
    }

    @Test
    public void testDeletarVaga() {
        doNothing().when(vagaRepository).deleteById(1L);

        vagaService.deletarVaga(1L);

        verify(vagaRepository).deleteById(1L);
    }
}
