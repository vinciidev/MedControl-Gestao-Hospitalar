package br.com.clinica.db;

import br.com.clinica.model.Medico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicoDAO {

    /**
     * Salva um novo registro de médico. Este método espera receber uma conexão
     * existente para poder fazer parte de uma transação maior.
     * @param medico O objeto Medico a ser salvo.
     * @param conn A conexão de banco de dados ativa.
     * @throws SQLException
     */

    public void salvar(Medico medico, Connection conn) throws SQLException {
        String sql = "INSERT INTO medicos (crm, especialidade, usuario_id) VALUES (?, ?, ?::uuid)";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, medico.getCrm());
            ps.setString(2, medico.getEspecialidade());
            ps.setString(3, medico.getUsuarioId());
            ps.executeUpdate();
        }
    }
}