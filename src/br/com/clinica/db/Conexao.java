package br.com.clinica.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class Conexao {
    private static HikariDataSource ds;

    static {
        HikariConfig config = new HikariConfig();

        config.setJdbcUrl("jdbc:postgresql://aws-1-us-east-2.pooler.supabase.com:6543/postgres?prepareThreshold=0");

        config.setUsername("postgres.tciizwfsfgojcccqiwhv");
        config.setPassword("@Apl197512");

        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);
        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");

        ds = new HikariDataSource(config);
    }

    public static Connection getConexao() throws SQLException {
        return ds.getConnection();
    }

    public static void fecharPool() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
            System.out.println("Pool de conexões com o banco de dados foi fechado.");
        }
    }
}