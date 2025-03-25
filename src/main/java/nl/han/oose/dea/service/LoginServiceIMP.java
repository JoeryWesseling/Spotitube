package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.data.dao.UserDao;
import nl.han.oose.dea.dto.LoginResponseDTO;

import java.util.UUID;


@ApplicationScoped
public class LoginServiceIMP implements LoginService{


    @Inject
    private TokenService tokenService;

    @Inject
    private UserDao userDAO;


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
