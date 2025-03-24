package nl.han.oose.dea.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.dto.LoginRequestDTO;
import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.exceptions.UnauthorizedException;
import nl.han.oose.dea.service.LoginService;


@Path("login")
public class Login {


    @Inject
    private LoginService loginService;

    public Login(){

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginRequest) {


        LoginResponseDTO responseDTO = loginService.checkCredentialsLogin(loginRequest.getUser(), loginRequest.getPassword());
        if (responseDTO != null) {
            return Response.ok(responseDTO).build();
        } else {
            try {
                throw new UnauthorizedException("Invalid credentials");
            } catch (UnauthorizedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @GET
    public String tester(){
        return "test";
}

}
