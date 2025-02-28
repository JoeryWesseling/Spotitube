package nl.han.oose.dea.service;

import nl.han.oose.dea.DTO.LoginResponseDto;

public class LoginService {

    static final String VALID_USER = "Frodo";
    static final String VALID_PASSWORD = "SamwiseGamgee";


    public LoginResponseDto checkCredentialsLogin(String username, String password) {
        if(VALID_USER.equals(username) && VALID_PASSWORD.equals(password)){
            String token = "My Precious";

            LoginResponseDto responseDto = new LoginResponseDto();
            responseDto.setToken(token);
            responseDto.setUser("Frodo Baggins");

            return responseDto;

        }
        return null;
    }
}
