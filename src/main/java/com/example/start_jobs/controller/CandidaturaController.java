package com.example.start_jobs.controller;

import com.example.start_jobs.dto.CandidaturaDTO;
import com.example.start_jobs.entity.Candidatura;
import com.example.start_jobs.exceptions.StatusAlreadyExistsException;
import com.example.start_jobs.service.CandidaturaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/candidaturas")
public class CandidaturaController {

    @Autowired
    private CandidaturaService candidaturaService;

    @PostMapping
    public ResponseEntity<?> criarCandidatura(@RequestBody CandidaturaDTO candidaturaDTO) {
        try {
            Candidatura candidaturaSalva = candidaturaService.criarCandidatura(candidaturaDTO);
            return ResponseEntity.ok().body("Candidatura criada com sucesso!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<CandidaturaDTO>> listarCandidaturas() {
        return ResponseEntity.ok(candidaturaService.listarCandidaturas());
    }


    @GetMapping("/{id}")
    public ResponseEntity<Optional<Candidatura>> buscarCandidaturaPorId(@PathVariable Long id) {
        return ResponseEntity.ok(candidaturaService.buscarCandidaturaPorId(id));
    }

    @GetMapping("/usuario/{id}")
    public ResponseEntity<List<CandidaturaDTO>> listarCandidaturasPorUsuario(@PathVariable Long id) {
        return ResponseEntity.ok(candidaturaService.listarCandidaturasPorUsuario(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Candidatura> atualizarCandidatura(
            @PathVariable Long id, @RequestBody Candidatura candidaturaAtualizada) {
        return ResponseEntity.ok(candidaturaService.atualizarCandidatura(id, candidaturaAtualizada));
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<CandidaturaDTO> adicionarNovoStatus(
            @PathVariable Long id,
            @RequestBody CandidaturaDTO candidaturaDTO) {
        try {
            CandidaturaDTO updatedCandidatura = candidaturaService.adicionarNovosStatus(id, candidaturaDTO.getStatusCandidatura());
            return ResponseEntity.ok(updatedCandidatura);
        } catch (StatusAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @PutMapping("/update-status/{id}")
    public ResponseEntity<CandidaturaDTO> atualizarStatus(
            @PathVariable Long id,
            @RequestBody CandidaturaDTO candidaturaDTO) {
        CandidaturaDTO updatedCandidatura = candidaturaService.atualizarStatus(id, candidaturaDTO);
        return ResponseEntity.ok(updatedCandidatura);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCandidatura(@PathVariable Long id) {
        candidaturaService.deletarCandidatura(id);
        return ResponseEntity.noContent().build();
    }
}
