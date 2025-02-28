package nl.han.oose.dea.service;

import nl.han.oose.dea.DTO.LoginResponseDTO;

import java.util.UUID;

public class LoginService {

    static final String VALID_USER = "Frodo";
    static final String VALID_PASSWORD = "SamwiseGamgee";


    public LoginResponseDTO checkCredentialsLogin(String username, String password) {
        if(VALID_USER.equals(username) && VALID_PASSWORD.equals(password)){
            String token = UUID.randomUUID().toString();

            LoginResponseDTO responseDTO = new LoginResponseDTO();
            responseDTO.setToken(token);
            responseDTO.setUser("Frodo Baggins");

            return responseDTO;

        }
        return null;
    }
}
