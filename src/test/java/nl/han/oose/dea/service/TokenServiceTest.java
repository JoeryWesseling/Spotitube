package nl.han.oose.dea.service;

import nl.han.oose.dea.data.dao.UserDAO;
import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceTest {

    @Mock
    private UserDAO userDAO;

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValidTokenReturnsTrue() throws UnauthorizedException {
        // Arrange
        String token = "validToken";
        LoginResponseDTO loginResponse = new LoginResponseDTO(1, token, "testUser");
        when(userDAO.verifyToken(token)).thenReturn(loginResponse);

        // Act
        boolean isValid = tokenService.isValidToken(token);

        // Assert
        assertTrue(isValid);
        verify(userDAO, times(1)).verifyToken(token);
    }

    @Test
    void testIsValidTokenReturnsFalse() throws UnauthorizedException {
        // Arrange
        String token = "invalidToken";
        when(userDAO.verifyToken(token)).thenThrow(new UnauthorizedException());

        // Act
        boolean isValid = tokenService.isValidToken(token);

        // Assert
        assertFalse(isValid);
        verify(userDAO, times(1)).verifyToken(token);
    }

    @Test
    void testGetTokenReturnsUserToken() {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        String expectedToken = "generatedToken";
        LoginResponseDTO loginResponse = new LoginResponseDTO(1, expectedToken, username);
        when(userDAO.getUserByUsername(username, password)).thenReturn(loginResponse);

        // Act
        String token = tokenService.getToken(username, password);

        // Assert
        assertEquals(expectedToken, token);
        verify(userDAO, times(1)).getUserByUsername(username, password);
    }

    @Test
    void testGetTokenReturnsNullIfUserNotFound() {
        // Arrange
        String username = "nonExistent";
        String password = "wrongPass";
        when(userDAO.getUserByUsername(username, password)).thenReturn(null);

        // Act
        String token = tokenService.getToken(username, password);

        // Assert
        assertNull(token);
        verify(userDAO, times(1)).getUserByUsername(username, password);
    }

    @Test
    void testGetUsernameFromTokenReturnsCorrectUsername() {
        // Arrange
        String token = "validToken";
        String expectedUsername = "testUser";
        when(userDAO.getUserByToken(token)).thenReturn(expectedUsername);

        // Act
        String username = tokenService.getUsernameFromToken(token);

        // Assert
        assertEquals(expectedUsername, username);
        verify(userDAO, times(1)).getUserByToken(token);
    }
}
