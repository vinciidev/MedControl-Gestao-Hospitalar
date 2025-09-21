package br.com.clinica.db;

import br.com.clinica.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

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