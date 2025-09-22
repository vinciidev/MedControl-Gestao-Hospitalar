package br.com.clinica.model;

public class Prontuario {
    private String id;
    private String anotacoesMedico;
    private String prescricao;
    private String examesSolicitados;
    private String consultaId;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getAnotacoesMedico() { return anotacoesMedico; }
    public void setAnotacoesMedico(String anotacoesMedico) { this.anotacoesMedico = anotacoesMedico; }
    public String getPrescricao() { return prescricao; }
    public void setPrescricao(String prescricao) { this.prescricao = prescricao; }
    public String getExamesSolicitados() { return examesSolicitados; }
    public void setExamesSolicitados(String examesSolicitados) { this.examesSolicitados = examesSolicitados; }
    public String getConsultaId() { return consultaId; }
    public void setConsultaId(String consultaId) { this.consultaId = consultaId; }
}