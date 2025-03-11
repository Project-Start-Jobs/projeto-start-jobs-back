package com.example.start_jobs.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;


import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender mailSender;  // Mock do JavaMailSender

    @InjectMocks
    private EmailService emailService;

    private String to;
    private String subject;
    private String content;

    @Before
    public void setUp() {
        to = "test@example.com";
        subject = "Test Subject";
        content = "This is a test email.";
    }

    @Test
    public void testSendEmail_Success() throws MessagingException {
        MimeMessageHelper helper = mock(MimeMessageHelper.class);
        MimeMessage message = mock(MimeMessage.class);

        when(mailSender.createMimeMessage()).thenReturn(message);



        emailService.sendEmail(to, subject, content);

        verify(mailSender, times(1)).send(message);
    }

    @Test(expected = RuntimeException.class)
    public void testSendEmail_MessagingException() throws MessagingException {
        MimeMessage message = mock(MimeMessage.class);
        when(mailSender.createMimeMessage()).thenReturn(message);

        doThrow(new RuntimeException(new MessagingException("Erro ao enviar email")))
                .when(mailSender).send(any(MimeMessage.class));

        emailService.sendEmail(to, subject, content);
    }

}