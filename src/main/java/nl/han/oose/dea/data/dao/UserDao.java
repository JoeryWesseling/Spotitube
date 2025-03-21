package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.exceptions.UnauthorizedException;



public interface UserDao {


     LoginResponseDTO getUserByUsername(String username, String password);

     boolean addToken(LoginResponseDTO user);

     LoginResponseDTO verifyToken(String token) throws UnauthorizedException;


     String getUserByToken(String token);
}
