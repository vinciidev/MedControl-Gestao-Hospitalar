package br.com.clinica.model;

import java.time.LocalDateTime;

public class Consulta {
    public enum StatusChamada {
        AGUARDANDO, CHAMANDO, EM_ATENDIMENTO, FINALIZADO
    }

    private String id;
    private Paciente paciente;
    private Usuario medico; // <-- MUDOU DE 'Medico' PARA 'Usuario'
    private LocalDateTime dataHora;
    private String status;
    private StatusChamada statusChamada;

    // Getters e Setters atualizados
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public Paciente getPaciente() { return paciente; }
    public void setPaciente(Paciente paciente) { this.paciente = paciente; }
    public Usuario getMedico() { return medico; } // <-- MUDOU
    public void setMedico(Usuario medico) { this.medico = medico; } // <-- MUDOU
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public StatusChamada getStatusChamada() { return statusChamada; }
    public void setStatusChamada(StatusChamada statusChamada) { this.statusChamada = statusChamada; }
}