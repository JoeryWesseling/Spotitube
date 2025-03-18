package nl.han.oose.dea.data.database;

import nl.han.oose.dea.exceptions.DatabaseException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DatabaseConnectionTest {

    private DatabaseConnection databaseConnection;

    private DbProperties dbProperties;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        databaseConnection = new DatabaseConnection();

        dbProperties = mock(DbProperties.class);
        when(dbProperties.getConnectionString()).thenReturn("jdbc:dummy");
        when(dbProperties.getUser()).thenReturn("dummyUser");
        when(dbProperties.getPassword()).thenReturn("dummyPass");
        when(dbProperties.getDriver()).thenReturn("java.lang.String");

        Field dbPropertiesField = DatabaseConnection.class.getDeclaredField("dbProperties");
        dbPropertiesField.setAccessible(true);
        dbPropertiesField.set(databaseConnection, dbProperties);
    }

    @Test
    void testGetConnectionSuccess() throws SQLException {
        // Arrange: Create a mock connection.
        Connection mockConnection = mock(Connection.class);
        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            )).thenReturn(mockConnection);

            // Act
            Connection conn = databaseConnection.getConnection();

            // Assert
            assertNotNull(conn);
            assertEquals(mockConnection, conn);
            mockedDriverManager.verify(() -> DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            ));
        }
    }


    @Test
    void testInitConnectionSuccess() throws Exception {
        // Arrange:
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            )).thenReturn(mockConnection);

            when(mockConnection.createStatement()).thenReturn(mockStatement);
            when(mockStatement.execute("SELECT 1")).thenReturn(true);

            databaseConnection.initConnection();

            // Assert:
            verify(mockConnection, times(1)).createStatement();
            verify(mockStatement, times(1)).execute("SELECT 1");
        }
    }

    @Test
    void testCloseConnection() throws Exception {
        // Arrange:
        Connection mockConnection = mock(Connection.class);
        Field connectionField = DatabaseConnection.class.getDeclaredField("connection");
        connectionField.setAccessible(true);
        connectionField.set(databaseConnection, mockConnection);

        // Act: Call closeConnection.
        databaseConnection.closeConnection();

        //assert
        verify(mockConnection, times(1)).close();
        assertNull(connectionField.get(databaseConnection));
    }

    @Test
    void testTestConnectionValid() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isValid(2)).thenReturn(true);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            )).thenReturn(mockConnection);

            // Act
            boolean isValid = databaseConnection.testConnection();

            // Assert
            assertTrue(isValid);
        }
    }

    @Test
    void testTestConnectionInvalid() throws SQLException {
        // Arrange
        Connection mockConnection = mock(Connection.class);
        when(mockConnection.isValid(2)).thenReturn(false);

        try (MockedStatic<DriverManager> mockedDriverManager = Mockito.mockStatic(DriverManager.class)) {
            mockedDriverManager.when(() -> DriverManager.getConnection(
                    dbProperties.getConnectionString(),
                    dbProperties.getUser(),
                    dbProperties.getPassword()
            )).thenReturn(mockConnection);

            // Act
            boolean isValid = databaseConnection.testConnection();

            // Assert
            assertFalse(isValid);
        }
    }
}
