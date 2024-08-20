import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public class DatabaseConfig {
    private static HikariDataSource dataSource;
    private static DatabaseConfig instance;

    private DatabaseConfig() {
        try (InputStream input = DatabaseConfig.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find db.properties");
                System.exit(1);
            }
            Properties properties = new Properties();
            properties.load(input);
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(properties.getProperty("db.url"));
            config.setUsername(properties.getProperty("db.username"));
            config.setPassword(properties.getProperty("db.password"));

            // Các cấu hình tùy chọn khác của HikariCP
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(5);
            config.setIdleTimeout(30000);
            config.setMaxLifetime(1800000);
            config.setConnectionTimeout(30000);

            // Tạo HikariDataSource
            dataSource = new HikariDataSource(config);
        } catch (IOException e) {
            throw new RuntimeException("Error initializing DatabaseConfig", e);
        }
    }

    public Connection connect() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Database connection error: " + e.getMessage());
            return null;
        }
    }

    public static DatabaseConfig getInstance() {
        if (instance == null) {
            synchronized (DatabaseConfig.class) {
                if (instance == null) {
                    instance = new DatabaseConfig();
                }
            }
        }
        return instance;
    }

    public void beginTransaction(Connection con) throws SQLException {
        if (con != null) {
            con.setAutoCommit(false);
        }
    }

    public void commitTransaction(Connection con) throws SQLException {
        if (con != null) {
            con.commit();
        }
    }

    public void rollbackTransaction(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                System.out.println("Error during transaction rollback: " + e.getMessage());
            }
        }
    }

    public static void closeResources(Connection con, PreparedStatement ps, ResultSet rs) {
        try {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (con != null) con.close();
        } catch (SQLException e) {
            System.out.println("Error closing resources: " + e.getMessage());
        }
    }
}
