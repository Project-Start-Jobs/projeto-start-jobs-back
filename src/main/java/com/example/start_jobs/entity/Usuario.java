package com.example.start_jobs.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "Usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idUsuario;

    private String nome;
    private String email;
    private String telefone;
    private String username;
    private String senha;
    private String imagem;

    @OneToMany(mappedBy = "usuario")
    private List<Candidatura> candidaturas;
}