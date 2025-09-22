package br.com.clinica.db;

import br.com.clinica.model.Paciente;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PacienteDAO {

    public List<Paciente> listarTodos() {
        String sql = "SELECT id, nome FROM pacientes ORDER BY nome ASC";
        List<Paciente> pacientes = new ArrayList<>();

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Paciente paciente = new Paciente();
                paciente.setId(rs.getString("id"));
                paciente.setNome(rs.getString("nome"));
                pacientes.add(paciente);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pacientes: " + e.getMessage(), e);
        }
        return pacientes;
    }

    public void salvar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (nome, cpf, data_nascimento, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getCpf());

            if (paciente.getDataNascimento() != null) {
                ps.setDate(3, java.sql.Date.valueOf(paciente.getDataNascimento()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            ps.setString(4, paciente.getCelular());

            ps.executeUpdate();
            System.out.println("Paciente " + paciente.getNome() + " salvo com sucesso!");
        }
    }
}