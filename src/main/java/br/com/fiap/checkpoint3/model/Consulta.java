package br.com.fiap.checkpoint3.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Consulta {
    private Long id;
    private Long profissionalId;
    private Long pacienteId;
    private LocalDateTime dataConsulta;
    private String statusConsulta; // AGENDADA, REALIZADA, CANCELADA
    private Integer quantidadeHoras;
    private BigDecimal valorConsulta;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Construtores
    public Consulta() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public Consulta(Long id, Long profissionalId, Long pacienteId, LocalDateTime dataConsulta, String statusConsulta, Integer quantidadeHoras, BigDecimal valorConsulta) {
        this.id = id;
        this.profissionalId = profissionalId;
        this.pacienteId = pacienteId;
        this.dataConsulta = dataConsulta;
        this.statusConsulta = statusConsulta;
        this.quantidadeHoras = quantidadeHoras;
        this.valorConsulta = valorConsulta;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getProfissionalId() { return profissionalId; }
    public void setProfissionalId(Long profissionalId) { this.profissionalId = profissionalId; }
    public Long getPacienteId() { return pacienteId; }
    public void setPacienteId(Long pacienteId) { this.pacienteId = pacienteId; }
    public LocalDateTime getDataConsulta() { return dataConsulta; }
    public void setDataConsulta(LocalDateTime dataConsulta) { this.dataConsulta = dataConsulta; }
    public String getStatusConsulta() { return statusConsulta; }
    public void setStatusConsulta(String statusConsulta) { this.statusConsulta = statusConsulta; }
    public Integer getQuantidadeHoras() { return quantidadeHoras; }
    public void setQuantidadeHoras(Integer quantidadeHoras) { this.quantidadeHoras = quantidadeHoras; }
    public BigDecimal getValorConsulta() { return valorConsulta; }
    public void setValorConsulta(BigDecimal valorConsulta) { this.valorConsulta = valorConsulta; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}