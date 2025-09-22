package br.com.clinica.db;

import br.com.clinica.model.Consulta;
import br.com.clinica.model.Paciente;
import br.com.clinica.model.Usuario;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ConsultaDAO {

    public void salvar(Consulta consulta) {
        String sql = "INSERT INTO consultas (id_paciente, id_medico, data_hora, status, status_chamada) " +
                "VALUES (?::uuid, (SELECT id FROM medicos WHERE usuario_id = ?::uuid), ?, ?::status_consulta, ?::status_chamada)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, consulta.getPaciente().getId());
            ps.setString(2, consulta.getMedico().getId());
            ps.setTimestamp(3, Timestamp.valueOf(consulta.getDataHora()));
            ps.setString(4, consulta.getStatus());
            ps.setString(5, consulta.getStatusChamada().name());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar consulta: " + e.getMessage(), e);
        }
    }

    public List<Consulta> listarTodas() {
        String sql = "SELECT " +
                "    c.id AS consulta_id, c.data_hora, c.status, c.status_chamada, " +
                "    p.id AS paciente_id, p.nome AS paciente_nome, " +
                "    u.id AS medico_id, u.nome AS medico_nome, u.role AS medico_role " +
                "FROM " +
                "    consultas c " +
                "JOIN " +
                "    pacientes p ON c.id_paciente = p.id " +
                "JOIN " +
                "    medicos m ON c.id_medico = m.id " +
                "JOIN " +
                "    usuarios u ON m.usuario_id = u.id " +
                "ORDER BY c.data_hora";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return carregarConsultas(rs);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar consultas: " + e.getMessage(), e);
        }
    }

    private List<Consulta> carregarConsultas(ResultSet rs) throws SQLException {
        List<Consulta> consultas = new ArrayList<>();
        while (rs.next()) {
            Paciente p = new Paciente();
            p.setId(rs.getString("paciente_id"));
            p.setNome(rs.getString("paciente_nome"));

            Usuario m = new Usuario();
            m.setId(rs.getString("medico_id"));
            m.setNome(rs.getString("medico_nome"));
            m.setRole(Usuario.Role.valueOf(rs.getString("medico_role")));

            Consulta c = new Consulta();
            c.setId(rs.getString("consulta_id"));
            c.setPaciente(p);
            c.setMedico(m);
            c.setDataHora(rs.getTimestamp("data_hora").toLocalDateTime());
            c.setStatus(rs.getString("status"));
            c.setStatusChamada(Consulta.StatusChamada.valueOf(rs.getString("status_chamada")));
            consultas.add(c);
        }
        return consultas;
    }

    public void atualizarStatusConsulta(String consultaId, String novoStatus) {
        String sql = "UPDATE consultas SET status = ?::status_consulta WHERE id = ?::uuid";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, novoStatus);
            ps.setString(2, consultaId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar status da consulta: " + e.getMessage(), e);
        }
    }

    public void excluir(String id) {
        String sql = "DELETE FROM consultas WHERE id = ?::uuid";
        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao excluir consulta: " + e.getMessage(), e);
        }
    }
}