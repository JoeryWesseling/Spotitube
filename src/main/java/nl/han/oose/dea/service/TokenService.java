package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.LoginResponseDTO;
import nl.han.oose.dea.data.DAO.UserDAO;
import nl.han.oose.dea.exceptions.UnauthorizedException;

@ApplicationScoped
public class TokenService {

    @Inject
    private UserDAO userDAO;

    public boolean isValidToken(String token) {
        try {
            userDAO.verifyToken(token);
            return true;
        } catch (UnauthorizedException e) {
            return false;
        }
    }


    public String getToken(String username, String password) {
        LoginResponseDTO user = userDAO.getUserByUsername(username, password);
        if (user != null) {
            return user.getToken();
        }
        return null;
    }

    public String getUsernameFromToken(String token){
        return userDAO.getUserByToken(token);
    }
}
