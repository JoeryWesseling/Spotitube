package nl.han.oose.dea.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.service.TokenService;

public class BaseResource {
    @Inject
    protected TokenService tokenService;


    protected String authenticate(String token) {
        if (token == null || !tokenService.isValidToken(token)) {
            throw new WebApplicationException(
                    Response.status(Response.Status.UNAUTHORIZED)
                            .entity("{\"error\": \"Invalid or missing token\"}")
                            .build()
            );
        }
        return tokenService.getUsernameFromToken(token);
    }
}
