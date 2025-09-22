package br.com.clinica.db;

import br.com.clinica.model.Paciente;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class PacienteDAO {


    public void salvar(Paciente paciente) throws SQLException {
        String sql = "INSERT INTO pacientes (nome, cpf, data_nascimento, telefone) VALUES (?, ?, ?, ?)";

        try (Connection conn = Conexao.getConexao();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, paciente.getNome());
            ps.setString(2, paciente.getCpf());

            // Converte LocalDate (Java 8+) para java.sql.Date
            if (paciente.getDataNascimento() != null) {
                ps.setDate(3, java.sql.Date.valueOf(paciente.getDataNascimento()));
            } else {
                ps.setNull(3, java.sql.Types.DATE);
            }

            ps.setString(4, paciente.getCelular());

            ps.executeUpdate();
            System.out.println("Paciente " + paciente.getNome() + " salvo com sucesso!");
        }
        // A exceção SQLException será tratada na classe do formulário
    }
}