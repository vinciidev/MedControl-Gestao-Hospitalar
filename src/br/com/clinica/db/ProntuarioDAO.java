package br.com.clinica.db;

import br.com.clinica.model.Prontuario;
import java.sql.*;

public class ProntuarioDAO {
    public void salvar(Prontuario prontuario) {
        String sqlCheck = "SELECT id FROM prontuarios WHERE consulta_id = ?::uuid";
        String sql;
        boolean existe = false;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement psCheck = conn.prepareStatement(sqlCheck)) {
            psCheck.setString(1, prontuario.getConsultaId());
            try (ResultSet rs = psCheck.executeQuery()) {
                if (rs.next()) {
                    existe = true;
                    prontuario.setId(rs.getString("id"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao verificar prontuário: " + e.getMessage(), e);
        }

        if (existe) {
            sql = "UPDATE prontuarios SET anotacoes_medico = ?, prescricao = ?, exames_solicitados = ? WHERE id = ?::uuid";
        } else {
            sql = "INSERT INTO prontuarios (anotacoes_medico, prescricao, exames_solicitados, consulta_id) VALUES (?, ?, ?, ?::uuid)";
        }
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, prontuario.getAnotacoesMedico());
            ps.setString(2, prontuario.getPrescricao());
            ps.setString(3, prontuario.getExamesSolicitados());
            if (existe) {
                ps.setString(4, prontuario.getId());
            } else {
                ps.setString(4, prontuario.getConsultaId());
            }
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar prontuário: " + e.getMessage(), e);
        }
    }

    public Prontuario buscarPorConsulta(String consultaId) {
        String sql = "SELECT * FROM prontuarios WHERE consulta_id = ?::uuid";
        Prontuario prontuario = null;
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, consultaId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    prontuario = new Prontuario();
                    prontuario.setId(rs.getString("id"));
                    prontuario.setConsultaId(rs.getString("consulta_id"));
                    prontuario.setAnotacoesMedico(rs.getString("anotacoes_medico"));
                    prontuario.setPrescricao(rs.getString("prescricao"));
                    prontuario.setExamesSolicitados(rs.getString("exames_solicitados"));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar prontuário: " + e.getMessage(), e);
        }
        return prontuario;
    }
}