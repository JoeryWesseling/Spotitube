package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.LoginMapper;
import nl.han.oose.dea.data.queries.UserQueries;
import nl.han.oose.dea.exceptions.DatabaseException;
import nl.han.oose.dea.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserDAOTest {

    private UserDAO userDAO;

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private LoginMapper loginMapper;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        userDAO = new UserDAO();

        Field dbConnField = UserDAO.class.getDeclaredField("databaseConnection");
        dbConnField.setAccessible(true);
        dbConnField.set(userDAO, databaseConnection);

        Field mapperField = UserDAO.class.getDeclaredField("loginMapper");
        mapperField.setAccessible(true);
        mapperField.set(userDAO, loginMapper);

        when(databaseConnection.getConnection()).thenReturn(connection);
    }

    @Test
    void testGetUserByUsernameFound() throws Exception {
        String username = "testUser";
        String password = "testPass";

        when(connection.prepareStatement(UserQueries.LOGIN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn(username);

        LoginResponseDTO response = userDAO.getUserByUsername(username, password);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(username, response.getUser());
    }

    @Test
    void testGetUserByUsernameNotFound() throws Exception {
        String username = "nonexistent";
        String password = "wrong";

        when(connection.prepareStatement(UserQueries.LOGIN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        LoginResponseDTO response = userDAO.getUserByUsername(username, password);
        assertNull(response);
    }

    @Test
    void testAddTokenSuccess() throws Exception {
        LoginResponseDTO user = new LoginResponseDTO(1, null, "testUser");
        user.setToken("validToken");

        when(connection.prepareStatement(UserQueries.ADD_TOKEN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        boolean result = userDAO.addToken(user);

        assertTrue(result);
        verify(preparedStatement).setString(1, "validToken");
        verify(preparedStatement).setInt(2, 1);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testAddTokenFailure() throws Exception {
        LoginResponseDTO user = new LoginResponseDTO(1, null, "testUser");
        user.setToken("invalidToken");

        when(connection.prepareStatement(UserQueries.ADD_TOKEN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        boolean result = userDAO.addToken(user);
        assertFalse(result);
    }

    @Test
    void testVerifyTokenSuccess() throws Exception {
        String token = "validToken";

        when(connection.prepareStatement(UserQueries.FETCH_USER_BY_TOKEN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        // Simulate a row being returned
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("username")).thenReturn("testUser");

        LoginResponseDTO response = userDAO.verifyToken(token);

        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals("testUser", response.getUser());
        assertEquals(token, response.getToken());
    }

    @Test
    void testVerifyTokenFailure() throws Exception {
        String token = "invalidToken";

        when(connection.prepareStatement(UserQueries.FETCH_USER_BY_TOKEN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        assertThrows(UnauthorizedException.class, () -> userDAO.verifyToken(token));
    }

    @Test
    void testGetUserByTokenFound() throws Exception {
        String token = "validToken";

        when(connection.prepareStatement(UserQueries.FETCH_USER_BY_TOKEN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getString("username")).thenReturn("testUser");

        String result = userDAO.getUserByToken(token);
        assertEquals("testUser", result);
    }

    @Test
    void testGetUserByTokenNotFound() throws Exception {
        String token = "nonexistentToken";

        when(connection.prepareStatement(UserQueries.FETCH_USER_BY_TOKEN_QUERY)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        String result = userDAO.getUserByToken(token);
        assertNull(result);
    }
}
