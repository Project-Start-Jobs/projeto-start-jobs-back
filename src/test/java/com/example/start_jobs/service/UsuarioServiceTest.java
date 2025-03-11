package com.example.start_jobs.service;

import com.example.start_jobs.entity.Usuario;
import com.example.start_jobs.repository.UsuarioRepository;
import com.example.start_jobs.service.UsuarioService;
import com.example.start_jobs.util.JwtUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private UsuarioService usuarioService;

    private Usuario usuario;

    @Before
    public void setUp() {
        usuario = new Usuario();
        usuario.setIdUsuario(1);
        usuario.setEmail("usuario@example.com");
        usuario.setSenha("senhaCriptografada");
    }

    @Test
    public void testCriarUsuario() {
        when(passwordEncoder.encode("senha123")).thenReturn("senhaCriptografada");

        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        usuario.setSenha("senha123");
        Usuario result = usuarioService.criarUsuario(usuario);

        verify(usuarioRepository).save(any(Usuario.class));

        assertNotNull(result);
        assertEquals(usuario.getEmail(), result.getEmail());
        assertEquals("senhaCriptografada", result.getSenha());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCriarUsuario_SenhaNula() {
        usuario.setSenha(null);
        usuarioService.criarUsuario(usuario);
    }

    @Test
    public void testBuscarUsuarioPorEmail() {
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.buscarUsuarioPorEmail("usuario@example.com");

        assertTrue(result.isPresent());
        assertEquals(usuario.getEmail(), result.get().getEmail());
    }

    @Test
    public void testAutenticarUsuario_Sucesso() {
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senha123", "senhaCriptografada")).thenReturn(true);
        when(jwtUtil.generateToken(usuario)).thenReturn("token-jwt");

        String token = usuarioService.autenticarUsuario("usuario@example.com", "senha123");

        assertNotNull(token);
        assertEquals("token-jwt", token);
    }

    @Test(expected = RuntimeException.class)
    public void testAutenticarUsuario_CredenciaisInvalidas() {
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.empty());

        usuarioService.autenticarUsuario("usuario@example.com", "senha123");
    }

    @Test(expected = RuntimeException.class)
    public void testAutenticarUsuario_SenhaIncorreta() {
        when(usuarioRepository.findByEmail("usuario@example.com")).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches("senhaErrada", "senhaCriptografada")).thenReturn(false);

        usuarioService.autenticarUsuario("usuario@example.com", "senhaErrada");
    }

    @Test
    public void testFindByPasswordResetToken() {
        String token = "reset-token";
        when(usuarioRepository.findByPasswordResetToken(token)).thenReturn(Optional.of(usuario));

        Optional<Usuario> result = usuarioService.findByPasswordResetToken(token);

        assertTrue(result.isPresent());
        assertEquals(usuario.getEmail(), result.get().getEmail());
    }
}
