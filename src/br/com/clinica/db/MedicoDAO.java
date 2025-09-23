package br.com.clinica.db;

import br.com.clinica.model.Medico;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MedicoDAO {

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