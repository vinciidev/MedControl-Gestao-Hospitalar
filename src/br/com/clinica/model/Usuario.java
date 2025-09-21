package br.com.clinica.model;

public class Usuario {
    private String id;
    private String username;
    private String password;
    private String nome;
    private Role role;

    public enum Role {
        MEDICO, RECEPCIONISTA, ADMINISTRADOR, TI
    }

    // Construtores
    public Usuario() {}

    public Usuario(String username, String password, String nome, Role role) {
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.role = role;
    }

    // Getters e Setters

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    // Métodos de verificação de permissão
    public boolean podeAcessarCadastroPacientes() {
        return role == Role.RECEPCIONISTA || role == Role.ADMINISTRADOR;
    }

    public boolean podeAcessarMarcacoes() {
        return role == Role.RECEPCIONISTA || role == Role.ADMINISTRADOR;
    }

    public boolean podeAcessarProntuario() {
        return role != Role.TI;
    }

    public boolean podeAcessarDashboard() {
        return role == Role.ADMINISTRADOR;
    }

    public boolean podeAcessarCadastroUsuarios() {
        return role == Role.ADMINISTRADOR || role == Role.TI;
    }

    public boolean ehMedico() {
        return role == Role.MEDICO;
    }

    @Override
    public String toString() {
        return (nome != null ? nome : username) + " (" + role + ")";
    }
}