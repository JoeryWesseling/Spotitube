package nl.han.oose.dea.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.dto.LoginRequestDTO;
import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginTest {

    @Mock
    private LoginService loginServiceMock;

    @InjectMocks
    private Login loginResource;

    private LoginRequestDTO loginRequestDTO;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);

        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUser("sauron");
        loginRequestDTO.setPassword("TheOneRingIsMine");
    }

    @Test
    void testLoginSucces() {
        // Arrange
        LoginResponseDTO mockResponse = new LoginResponseDTO();
        mockResponse.setUser(loginRequestDTO.getUser());
        mockResponse.setToken("validToken");

        when(loginServiceMock.checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword()))
                .thenReturn(mockResponse);

        // Act
        Response response = loginResource.login(loginRequestDTO);
        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(loginResponseDTO);
        assertEquals(loginRequestDTO.getUser(), loginResponseDTO.getUser());
        assertEquals("validToken", loginResponseDTO.getToken());

        verify(loginServiceMock, times(1)).checkCredentialsLogin(
                loginRequestDTO.getUser(), loginRequestDTO.getPassword());
    }

    @Test
    void testLoginFailure() {
        // Arrange
        when(loginServiceMock.checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword()))
                .thenReturn(null);

        // Act
        Response response = loginResource.login(loginRequestDTO);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());

        // Verify
        verify(loginServiceMock, times(1)).checkCredentialsLogin(
                loginRequestDTO.getUser(), loginRequestDTO.getPassword());
    }

    @Test
    void testLoginTesterGet() {
        // Act:
        String result = loginResource.tester();

        // Assert:
        assertEquals("test", result);
    }


    @Test
    void testLoginWithEmptyCredentials() {
        // Arrange:
        LoginRequestDTO emptyLoginRequest = new LoginRequestDTO();
        emptyLoginRequest.setUser("");
        emptyLoginRequest.setPassword("");

        when(loginServiceMock.checkCredentialsLogin("", ""))
                .thenReturn(null);

        // Act
        Response response = loginResource.login(emptyLoginRequest);

        // Assert:
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());

        verify(loginServiceMock, times(1)).checkCredentialsLogin("", "");
    }
}
