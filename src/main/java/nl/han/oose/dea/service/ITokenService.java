package nl.han.oose.dea.service;

public interface ITokenService {

    public boolean isValidToken(String token);

    public String getToken(String username, String password);
}
