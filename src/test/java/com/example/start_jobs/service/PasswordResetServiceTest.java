package com.example.start_jobs.service;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import com.example.start_jobs.entity.Usuario;
import com.example.start_jobs.repository.UsuarioRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

public class PasswordResetServiceTest {

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PasswordResetService passwordResetService;

    private Usuario usuario;
    private String validToken;
    private String expiredToken;
    private BCryptPasswordEncoder passwordEncoder;



    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();

        usuario = new Usuario();
        usuario.setEmail("test@example.com");
        usuario.setSenha("senha123");

        validToken = "validToken";
        expiredToken = "expiredToken";

        usuario.setPasswordResetToken(validToken);
        usuario.setPasswordResetExpiration(LocalDateTime.now().plusHours(1));

        Usuario expiredUser = new Usuario();
        expiredUser.setEmail("test@example.com");
        expiredUser.setPasswordResetToken(expiredToken);
        expiredUser.setPasswordResetExpiration(LocalDateTime.now().minusHours(1));



        usuario.setPasswordResetToken(validToken);
        usuario.setPasswordResetExpiration(LocalDateTime.now().plusHours(1));
    }

    @Test
    public void testSendPasswordResetEmail_Success() {
        when(usuarioService.buscarUsuarioPorEmail("test@example.com")).thenReturn(Optional.of(usuario));

        passwordResetService.sendPasswordResetEmail("test@example.com");

        verify(emailService, times(1)).sendEmail(eq("test@example.com"), anyString(), anyString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSendPasswordResetEmail_UserNotFound() {
        when(usuarioService.buscarUsuarioPorEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        passwordResetService.sendPasswordResetEmail("nonexistent@example.com");
    }

    @Test
    public void testResetPassword_Success() {
        when(usuarioService.findByPasswordResetToken(validToken)).thenReturn(Optional.of(usuario));

        passwordResetService.resetPassword(validToken, "newPassword123");

        verify(usuarioRepository, times(1)).save(usuario);

        Assert.assertTrue(passwordEncoder.matches("newPassword123", usuario.getSenha()));

        assertNull(usuario.getPasswordResetToken());
        assertNull(usuario.getPasswordResetExpiration());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPassword_InvalidToken() {
        when(usuarioService.findByPasswordResetToken("invalidToken")).thenReturn(Optional.empty());

        passwordResetService.resetPassword("invalidToken", "newPassword123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testResetPassword_ExpiredToken() {
        LocalDateTime expiredTime = LocalDateTime.now().minusDays(1);  
        usuario.setPasswordResetExpiration(expiredTime);

        when(usuarioService.findByPasswordResetToken(expiredToken)).thenReturn(Optional.of(usuario));

        passwordResetService.resetPassword(expiredToken, "newPassword123");
    }

}
