package nl.han.oose.dea.service;

import nl.han.oose.dea.dto.LoginResponseDTO;

public interface LoginService {
     LoginResponseDTO checkCredentialsLogin(String username, String password);
}
