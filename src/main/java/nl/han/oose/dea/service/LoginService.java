package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.LoginResponseDTO;
import nl.han.oose.dea.data.DAO.UserDAO;

import java.util.UUID;


@ApplicationScoped
public class LoginService {


    @Inject
    private TokenService tokenService;

    @Inject
    private UserDAO userDAO;


    public LoginResponseDTO checkCredentialsLogin(String username, String password) {
        LoginResponseDTO user = userDAO.getUserByUsername(username, password);

        if (user != null) {
            String token = tokenService.getToken(username, password);

            if (token == null) {
                token = UUID.randomUUID().toString();
                user.setToken(token);
                userDAO.addToken(user);
            }
            return new LoginResponseDTO(user.getId(),token, user.getUser());
        }
        return null;

    }
}
