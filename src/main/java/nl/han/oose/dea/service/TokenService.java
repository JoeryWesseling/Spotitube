package nl.han.oose.dea.service;

public interface TokenService {

     boolean isValidToken(String token);

     String getToken(String username, String password);
}
