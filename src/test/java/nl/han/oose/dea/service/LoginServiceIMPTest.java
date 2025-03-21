package nl.han.oose.dea.service;


import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.data.dao.UserDaoIMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginServiceIMPTest {

    @Mock
    private TokenServiceIMP tokenServiceIMP;

    @Mock
    private UserDaoIMP userDaoIMP;

    @InjectMocks
    private LoginServiceIMP loginServiceIMP;

    private final int ID = 1;
    private final String VALIDUSERNAME = "Sauron";
    private final String EXISTINGTOKEN = "existing-token";
    private final String VALIDPASSWORD = "Sauron";
    private final String NEWTOKEN = "new-generated-token";

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSuccessfulLoginWithExistingToken() {
        // Arrange
        LoginResponseDTO mockUser = new LoginResponseDTO(ID, EXISTINGTOKEN, VALIDUSERNAME);
        when(userDaoIMP.getUserByUsername(VALIDUSERNAME, VALIDPASSWORD)).thenReturn(mockUser);
        when(tokenServiceIMP.getToken(VALIDUSERNAME, VALIDPASSWORD)).thenReturn(EXISTINGTOKEN);

        // Act
        LoginResponseDTO response = loginServiceIMP.checkCredentialsLogin(VALIDUSERNAME, VALIDPASSWORD);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertEquals(EXISTINGTOKEN, response.getToken());
        assertEquals(VALIDUSERNAME, response.getUser());

        // Verify interactions
        verify(userDaoIMP, times(1)).getUserByUsername(VALIDUSERNAME, VALIDPASSWORD);
        verify(tokenServiceIMP, times(1)).getToken(VALIDUSERNAME, VALIDPASSWORD);
        verify(userDaoIMP, never()).addToken(any());
    }

    @Test
    void testSuccessfulLoginWithNewToken() {
        // Arrange
        LoginResponseDTO mockUser = new LoginResponseDTO(ID, null, VALIDUSERNAME);
        when(userDaoIMP.getUserByUsername(VALIDUSERNAME, VALIDPASSWORD)).thenReturn(mockUser);
        when(tokenServiceIMP.getToken(VALIDUSERNAME, VALIDPASSWORD)).thenReturn(null);
        doAnswer(invocation -> {
            LoginResponseDTO userArg = invocation.getArgument(0);
            assertNotNull(userArg.getToken());
            return null;
        }).when(userDaoIMP).addToken(mockUser);

        // Act
        LoginResponseDTO response = loginServiceIMP.checkCredentialsLogin(VALIDUSERNAME, VALIDPASSWORD);

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getId());
        assertNotNull(response.getToken());
        assertEquals(VALIDUSERNAME, response.getUser());

        // Verify interactions
        verify(userDaoIMP, times(1)).getUserByUsername(VALIDUSERNAME, VALIDPASSWORD);
        verify(tokenServiceIMP, times(1)).getToken(VALIDUSERNAME, VALIDPASSWORD);
        verify(userDaoIMP, times(1)).addToken(mockUser);
    }

    @Test
    void testFailedLogin() {
        // Arrange
        when(userDaoIMP.getUserByUsername(VALIDUSERNAME, VALIDPASSWORD)).thenReturn(null);

        // Act
        LoginResponseDTO response = loginServiceIMP.checkCredentialsLogin(VALIDUSERNAME, VALIDPASSWORD);

        // Assert
        assertNull(response);

        // Verify interactions
        verify(userDaoIMP, times(1)).getUserByUsername(VALIDUSERNAME, VALIDPASSWORD);
        verify(tokenServiceIMP, never()).getToken(any(), any());
        verify(userDaoIMP, never()).addToken(any());
    }
}


