package nl.han.oose.dea.service;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenService {

    private static final String TOKEN = "Sauron";

    public boolean isValidToken(String token){
        return TOKEN.equals(token);
    }

    public String getTOKEN() {
        return TOKEN;
    }
}
