package nl.han.oose.dea.resources;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.LoginRequestDTO;
import nl.han.oose.dea.DTO.LoginResponseDto;
import nl.han.oose.dea.service.LoginService;


@Path("/login")
public class Login {

    private final LoginService loginService = new LoginService(); // Call LoginService


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginrequest) {

        LoginResponseDto responseDto = loginService.checkCredentialsLogin(loginrequest.getUser(),loginrequest.getPassword());

        if(responseDto != null){
            return Response.ok(responseDto).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"message\": \"Invalid credentials\"}")
                    .build();
        }
    }
}
