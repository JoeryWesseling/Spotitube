package nl.han.oose.dea.resources;


import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.LoginRequestDTO;
import nl.han.oose.dea.DTO.LoginResponseDTO;
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

        when(loginServiceMock.checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword()))
                .thenReturn(mockResponse);

        //act
        Response response = loginResource.login(loginRequestDTO);
        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) response.getEntity();

        //assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(loginResponseDTO);
        assertEquals(loginRequestDTO.getUser(), loginResponseDTO.getUser());

        verify(loginServiceMock, times(1)).checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword());
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

        // Verify that loginServiceMock was actually called
        verify(loginServiceMock, times(1)).checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword());
    }

}

