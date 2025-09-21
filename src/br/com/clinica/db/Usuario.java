package br.com.clinica.db;

import br.com.clinica.db.Conexao;
import br.com.clinica.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {
    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id, username, role FROM usuarios WHERE username = ? AND password = ?";
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
                    usuario.setRole(Usuario.Role.valueOf(rs.getString("role")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
        return usuario;
    }

    public String salvarNovoUsuario(Usuario usuario, Connection conn) throws SQLException {
        String generatedId = null;
        String sql = "INSERT INTO usuarios (username, password, role) VALUES (?, ?, ?::roles) RETURNING id";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, usuario.getUsername());
            ps.setString(2, usuario.getPassword());
            ps.setString(3, usuario.getRole().name());
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    generatedId = rs.getString(1);
                }
            }
        }
        return generatedId;
    }
}