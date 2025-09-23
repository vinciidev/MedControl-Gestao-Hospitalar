package br.com.clinica.db;

import br.com.clinica.model.Medico;
import br.com.clinica.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class UsuarioDAO {
    public Usuario autenticar(String username, String password) {
        String sql = "SELECT id, nome, username, role FROM usuarios WHERE username = ? AND password = ?";
        Usuario usuario = null;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    usuario = new Usuario();
                    usuario.setId(rs.getString("id"));
                    usuario.setNome(rs.getString("nome"));
                    usuario.setUsername(rs.getString("username"));
                    usuario.setRole(Usuario.Role.valueOf(rs.getString("role")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao autenticar usuário: " + e.getMessage(), e);
        }
        return usuario;
    }

    public boolean usuarioJaExiste(String username) throws SQLException {
        String sql = "SELECT 1 FROM usuarios WHERE username = ?";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void salvarUsuario(Usuario usuario) throws SQLException {
        String sqlUsuario = "INSERT INTO usuarios (nome, username, password, role) VALUES (?, ?, ?, ?::roles) RETURNING id";
        Connection conn = null;

        try {
            conn = Conexao.getConexao();
            conn.setAutoCommit(false);

            String usuarioId;
            try (PreparedStatement ps = conn.prepareStatement(sqlUsuario)) {
                ps.setString(1, usuario.getNome());
                ps.setString(2, usuario.getUsername());
                ps.setString(3, usuario.getPassword());
                ps.setString(4, usuario.getRole().name());
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    usuarioId = rs.getString(1);
                    usuario.setId(usuarioId);
                } else {
                    throw new SQLException("Falha ao criar usuário, nenhum ID retornado.");
                }
            }


            if (usuario.getRole() == Usuario.Role.MEDICO) {

                Random random = new Random();
                String crmAleatorio = String.format("%05d", random.nextInt(100000));

                Medico medico = new Medico();
                medico.setCrm(crmAleatorio);

                medico.setEspecialidade("Clinico Geral");
                medico.setUsuarioId(usuarioId);
                new MedicoDAO().salvar(medico, conn);
            }

            conn.commit();

        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }

    public List<Usuario> listarMedicos() {
        List<Usuario> medicos = new ArrayList<>();
        String sql = "SELECT id, nome, username, role FROM usuarios WHERE role = 'MEDICO' ORDER BY nome";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Usuario medico = new Usuario();
                medico.setId(rs.getString("id"));
                medico.setNome(rs.getString("nome"));
                medico.setUsername(rs.getString("username"));
                medico.setRole(Usuario.Role.valueOf(rs.getString("role")));
                medicos.add(medico);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar médicos: " + e.getMessage(), e);
        }
        return medicos;
    }
}