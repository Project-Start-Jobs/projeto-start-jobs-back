package com.example.start_jobs.entity;

import com.example.start_jobs.dto.CandidaturaDTO;
import com.example.start_jobs.dto.StatusCandidaturaDTO;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "status_candidatura")
@AllArgsConstructor
@NoArgsConstructor
public class StatusCandidatura {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idStatus;

    private String label;
    private boolean approved;
    private boolean rejected;

    @ManyToOne
    @JoinColumn(name = "id_candidatura", nullable = false)
    private Candidatura candidatura;

    @Column(name = "data_status")
    private LocalDateTime dataStatus;



    public StatusCandidatura(StatusCandidaturaDTO statusCandidaturaDTO) {
        this.idStatus = statusCandidaturaDTO.getId();
        this.label = statusCandidaturaDTO.getLabel();
        this.approved = statusCandidaturaDTO.isApproved();
        this.rejected = statusCandidaturaDTO.isRejected();

        this.candidatura = new Candidatura(statusCandidaturaDTO.getCandidatura());

    }
    public StatusCandidatura(StatusCandidaturaDTO statusCandidatura, Candidatura candidatura) {

        this.idStatus = statusCandidatura.getId();
        this.label = statusCandidatura.getLabel();
        this.approved = statusCandidatura.isApproved();
        this.rejected = statusCandidatura.isRejected();
        this.candidatura =candidatura;

    }



}
