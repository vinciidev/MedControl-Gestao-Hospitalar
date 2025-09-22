package br.com.clinica.model;

import java.time.LocalDate;

public class Paciente {
    private String id;
    private String nome;
    private String cpf;
    private LocalDate dataNascimento;
    private String celular;

    // Getters e Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCpf() { return cpf; }
    public void setCpf(String cpf) { this.cpf = cpf; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public String getCelular() { return celular; }
    public void setCelular(String celular) { this.celular = celular; }
}