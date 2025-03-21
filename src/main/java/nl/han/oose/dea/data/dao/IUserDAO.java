package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.exceptions.UnauthorizedException;



public interface IUserDAO {


    public LoginResponseDTO getUserByUsername(String username, String password);

    public boolean addToken(LoginResponseDTO user);

    public LoginResponseDTO verifyToken(String token) throws UnauthorizedException;


    public String getUserByToken(String token);
}
