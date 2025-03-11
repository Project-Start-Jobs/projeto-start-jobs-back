package com.example.start_jobs.service;

import com.example.start_jobs.entity.Dicas;
import com.example.start_jobs.repository.DicasRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) 
class DicasServiceTest {

    @Mock
    private DicasRepository dicasRepository;

    @InjectMocks
    private DicasService dicasService;

    private Dicas dica;
    private final Long id = 1L;

    @BeforeEach
    void setUp() {
        dica = new Dicas();
        dica.setIdDica(Integer.parseInt(String.valueOf(id)));
        dica.setTitulo("Dica de Teste");
        dica.setDescricao("Descrição da dica de teste");
        dica.setCategoria("Categoria Teste");
        dica.setImagem("imagem-teste.jpg");
        dica.setDataPublicacao(LocalDateTime.now());
    }

    @Test
    void criarDica_DeveCriarUmaNovaDica() {
        when(dicasRepository.save(any(Dicas.class))).thenReturn(dica);

        Dicas novaDica = dicasService.criarDica(dica);

        assertNotNull(novaDica);
        assertEquals(dica.getTitulo(), novaDica.getTitulo());
        assertEquals(dica.getDescricao(), novaDica.getDescricao());
        assertEquals(dica.getCategoria(), novaDica.getCategoria());
        assertEquals(dica.getImagem(), novaDica.getImagem());
        assertNotNull(novaDica.getDataPublicacao());

        verify(dicasRepository, times(1)).save(any(Dicas.class));  // Verifica que o save foi chamado uma vez
    }

    @Test
    void listarDicas_DeveRetornarUmaListaDeDicas() {
        when(dicasRepository.findAll()).thenReturn(List.of(dica));

        var dicasList = dicasService.listarDicas();

        assertNotNull(dicasList);
        assertEquals(1, dicasList.size());
        assertEquals(dica.getTitulo(), dicasList.get(0).getTitulo());
    }

    @Test
    void buscarDicaPorId_DeveRetornarUmaDicaQuandoEncontrada() {
        when(dicasRepository.findById(id)).thenReturn(Optional.of(dica));

        Optional<Dicas> dicaEncontrada = dicasService.buscarDicaPorId(id);

        assertTrue(dicaEncontrada.isPresent());
        assertEquals(dica.getIdDica(), dicaEncontrada.get().getIdDica());
        assertEquals(dica.getTitulo(), dicaEncontrada.get().getTitulo());
    }

    @Test
    void buscarDicaPorId_DeveRetornarVazioQuandoNaoEncontrada() {
        when(dicasRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Dicas> dicaNaoEncontrada = dicasService.buscarDicaPorId(id);

        assertFalse(dicaNaoEncontrada.isPresent());
    }

    @Test
    void atualizarDica_DeveAtualizarAInformacaoDeUmaDica() {
        Dicas dicaAtualizada = new Dicas();
        dicaAtualizada.setIdDica(Integer.parseInt(String.valueOf(id)));
        dicaAtualizada.setTitulo("Dica Atualizada");
        dicaAtualizada.setDescricao("Descrição Atualizada");
        dicaAtualizada.setCategoria("Categoria Atualizada");
        dicaAtualizada.setImagem("imagem-atualizada.jpg");

        when(dicasRepository.findById(id)).thenReturn(Optional.of(dica));
        when(dicasRepository.save(any(Dicas.class))).thenReturn(dicaAtualizada);

        Dicas dicaResultado = dicasService.atualizarDica(id, dicaAtualizada);

        assertNotNull(dicaResultado);
        assertEquals(dicaAtualizada.getTitulo(), dicaResultado.getTitulo());
        assertEquals(dicaAtualizada.getDescricao(), dicaResultado.getDescricao());
        assertEquals(dicaAtualizada.getCategoria(), dicaResultado.getCategoria());
        assertEquals(dicaAtualizada.getImagem(), dicaResultado.getImagem());

        verify(dicasRepository, times(1)).save(any(Dicas.class));  // Verifica que o save foi chamado uma vez
    }

    @Test
    void atualizarDica_DeveLancarExcecaoQuandoDicaNaoEncontrada() {

        Dicas dicaAtualizada = new Dicas();
        dicaAtualizada.setIdDica(Integer.parseInt(String.valueOf(id)));
        dicaAtualizada.setTitulo("Dica Atualizada");
        dicaAtualizada.setDescricao("Descrição Atualizada");
        dicaAtualizada.setCategoria("Categoria Atualizada");
        dicaAtualizada.setImagem("imagem-atualizada.jpg");

        when(dicasRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> dicasService.atualizarDica(id, dicaAtualizada));
    }

    @Test
    void deletarDica_DeveDeletarUmaDica() {
        doNothing().when(dicasRepository).deleteById(id);

        dicasService.deletarDica(id);


        verify(dicasRepository, times(1)).deleteById(id);
    }
}
