package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;


@ApplicationScoped
public class DatabaseConnection {
    private Connection connection;

    @Inject
    private DbProperties dbProperties;

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            );
        } catch (SQLException e) {
            throw new DatabaseException("Kan geen databaseverbinding maken", e);
        }
    }


    public void initConnection() {
        try {
            Class.forName(dbProperties.getDriver());
            connection = DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            );
            System.out.println("✅ Databaseverbinding geslaagd!");

            // Testquery uitvoeren
            try (Statement stmt = connection.createStatement()) {
                stmt.execute("SELECT 1");
                System.out.println("✅ Testquery succesvol uitgevoerd");
            }

        } catch (ClassNotFoundException | SQLException e) {
            throw new DatabaseException("Databaseverbinding mislukt", e);
        }
    }


    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                throw new DatabaseException("fout bij het sluiten",e);
            }
        }
    }
    public boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && conn.isValid(2);  // Test the connection validity for 2 seconds
        } catch (SQLException e) {
            return false;  // Return false if any exception occurs
        }
    }

}
