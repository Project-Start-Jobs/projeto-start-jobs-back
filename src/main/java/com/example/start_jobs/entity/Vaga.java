package com.example.start_jobs.entity;

import com.example.start_jobs.dto.StatusCandidaturaDTO;
import com.example.start_jobs.dto.VagaDTO;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name = "Vaga")
public class Vaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idVaga;

    private String titulo;

    @Lob
    @Column(length = 1000)
    private String descricao;
    private String empresa;
    private String localizacao;
    private String senioridade;
    private String modeloTrabalho;
    @OneToMany(mappedBy = "vaga")
    private List<Candidatura> candidaturas;
    @Column(name = "data_criacao")
    private LocalDate dataCriacao;

    private String url;

    public Vaga(VagaDTO vaga) {
        this.idVaga = vaga.getId();
        this.titulo = vaga.getTitulo();
        this.descricao = vaga.getDescricao();
        this.empresa = vaga.getEmpresa();
        this.localizacao = vaga.getLocalizacao();
        this.senioridade = vaga.getSenioridade();
        this.modeloTrabalho = vaga.getModeloTrabalho();
        this.candidaturas = vaga.getCandidaturas().stream()
                .map(Candidatura::new)
                .collect(Collectors.toList());
        this.dataCriacao = vaga.getDataCriacao();
        this.url = vaga.getUrl();
    }
}
