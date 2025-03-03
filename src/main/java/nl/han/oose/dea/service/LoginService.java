package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.LoginResponseDTO;

import java.util.UUID;


@ApplicationScoped
public class LoginService {

    static final String VALID_USER = "Frodo";
    static final String VALID_PASSWORD = "SamwiseGamgee";

    @Inject
    private TokenService tokenService;


    public LoginResponseDTO checkCredentialsLogin(String username, String password) {
        if(VALID_USER.equals(username) && VALID_PASSWORD.equals(password)){
            return new LoginResponseDTO(tokenService.getTOKEN(),"Frodo Baggins");
        }
        return null;
    }
}
