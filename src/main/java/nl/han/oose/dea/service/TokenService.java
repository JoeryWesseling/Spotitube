package nl.han.oose.dea.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DAO.TokenDAO;

@ApplicationScoped
public class TokenService {

    @Inject
    private TokenDAO tokenDAO;


    public boolean isValidToken(String token){
        return tokenDAO.isValidToken(token);
    }

    public String getTOKEN() {
        return tokenDAO.getToken();
    }
}
