package nl.han.oose.dea.resources;


import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.LoginRequestDTO;
import nl.han.oose.dea.DTO.LoginResponseDTO;
import nl.han.oose.dea.service.LoginService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginTest {

    private Login loginResource;
    private LoginRequestDTO loginRequestDTO;
    private LoginService loginServiceMock;

    @BeforeEach
    void setup(){
        this.loginServiceMock = mock(LoginService.class);
        this.loginResource = new Login(this.loginServiceMock);  // Inject mock service
        this.loginRequestDTO = new LoginRequestDTO();
        this.loginRequestDTO.setUser("Sauron");
        this.loginRequestDTO.setPassword("TheOneRingIsMine");
    }

    @Test
    void testLoginSucces(){
        // Arrange
        LoginResponseDTO mockResponse = new LoginResponseDTO();
        mockResponse.setUser(loginRequestDTO.getUser());

        when(loginServiceMock.checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword()))
                .thenReturn(mockResponse);

        // Act
        Response response = this.loginResource.login(this.loginRequestDTO);
        LoginResponseDTO loginResponseDTO = (LoginResponseDTO) response.getEntity();

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(this.loginRequestDTO.getUser(), loginResponseDTO.getUser());
    }

    @Test
    void testLoginFailure() {
        when(loginServiceMock.checkCredentialsLogin(loginRequestDTO.getUser(), loginRequestDTO.getPassword()))
                .thenReturn(null);

        // Act
        Response response = this.loginResource.login(this.loginRequestDTO);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertNull(response.getEntity());
    }

}

