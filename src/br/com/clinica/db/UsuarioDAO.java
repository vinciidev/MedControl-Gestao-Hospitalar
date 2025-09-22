package br.com.clinica.db;

import br.com.clinica.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;

public class UsuarioDAO {

    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id, username, nome , password, role FROM usuarios WHERE username = ? AND password = ?";
        Usuario usuario = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getString("id"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setNome(rs.getString("nome"));

                    // Converter string para enum
                    String roleStr = rs.getString("role");
                    try {
                        usuario.setRole(Usuario.Role.valueOf(roleStr.toUpperCase()));
                    } catch (IllegalArgumentException e) {
                        System.err.println("Role inválido no banco: " + roleStr);
                        return null;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao autenticar usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return usuario;
    }

    public List<Usuario> listarMedicos() {
        List<Usuario> medicos = new ArrayList<>();
        // A query busca todos os usuários cuja role seja 'MEDICO'
        String sql = "SELECT id, username, nome, role FROM usuarios WHERE role = 'MEDICO' ORDER BY nome";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Usuario medico = new Usuario();
                medico.setId(rs.getString("id"));
                medico.setUsername(rs.getString("username"));
                medico.setNome(rs.getString("nome"));
                medico.setRole(Usuario.Role.valueOf(rs.getString("role")));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos: " + e.getMessage(), e);
        }
        return medicos;
    }


    public boolean verificarConexao() {
        try (Connection conn = Conexao.getConexao()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            System.err.println("Erro ao verificar conexão: " + e.getMessage());
            return false;
        }
    }

    public String salvarNovoUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (username, password, nome, role) VALUES (?, ?, ?, ?::roles) RETURNING id";
        String generatedId = null;

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getNome());
            ps.setString(4, usuario.getRole().name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    generatedId = rs.getString(1);
                    usuario.setId(generatedId);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar usuário: " + e.getMessage());
            e.printStackTrace();
        }

        return generatedId;
    }
}

