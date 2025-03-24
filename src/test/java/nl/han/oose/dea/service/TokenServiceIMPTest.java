package nl.han.oose.dea.service;

import nl.han.oose.dea.data.dao.UserDaoIMP;
import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.exceptions.UnauthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TokenServiceIMPTest {

    @Mock
    private UserDaoIMP userDaoIMP;

    @InjectMocks
    private TokenServiceIMP tokenServiceIMP;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testIsValidTokenReturnsTrue() throws UnauthorizedException {
        // Arrange
        String token = "validToken";
        LoginResponseDTO loginResponse = new LoginResponseDTO(1, token, "testUser");
        when(userDaoIMP.verifyToken(token)).thenReturn(loginResponse);

        // Act
        boolean isValid = tokenServiceIMP.isValidToken(token);

        // Assert
        assertTrue(isValid);
        verify(userDaoIMP, times(1)).verifyToken(token);
    }

    @Test
    void testIsValidTokenReturnsFalse() throws UnauthorizedException {
        // Arrange
        String token = "invalidToken";
        when(userDaoIMP.verifyToken(token)).thenThrow(new UnauthorizedException("invalid token"));

        // Act
        boolean isValid = tokenServiceIMP.isValidToken(token);

        // Assert
        assertFalse(isValid);
        verify(userDaoIMP, times(1)).verifyToken(token);
    }

    @Test
    void testGetTokenReturnsUserToken() {
        // Arrange
        String username = "testUser";
        String password = "testPass";
        String expectedToken = "generatedToken";
        LoginResponseDTO loginResponse = new LoginResponseDTO(1, expectedToken, username);
        when(userDaoIMP.getUserByUsername(username, password)).thenReturn(loginResponse);

        // Act
        String token = tokenServiceIMP.getToken(username, password);

        // Assert
        assertEquals(expectedToken, token);
        verify(userDaoIMP, times(1)).getUserByUsername(username, password);
    }

    @Test
    void testGetTokenReturnsNullIfUserNotFound() {
        // Arrange
        String username = "nonExistent";
        String password = "wrongPass";
        when(userDaoIMP.getUserByUsername(username, password)).thenReturn(null);

        // Act
        String token = tokenServiceIMP.getToken(username, password);

        // Assert
        assertNull(token);
        verify(userDaoIMP, times(1)).getUserByUsername(username, password);
    }

    @Test
    void testGetUsernameFromTokenReturnsCorrectUsername() {
        // Arrange
        String token = "validToken";
        String expectedUsername = "testUser";
        when(userDaoIMP.getUserByToken(token)).thenReturn(expectedUsername);

        // Act
        String username = tokenServiceIMP.getUsernameFromToken(token);

        // Assert
        assertEquals(expectedUsername, username);
        verify(userDaoIMP, times(1)).getUserByToken(token);
    }
}
