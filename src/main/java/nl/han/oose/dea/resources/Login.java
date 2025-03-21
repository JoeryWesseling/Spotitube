package nl.han.oose.dea.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.dto.LoginRequestDTO;
import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.service.LoginService;
import nl.han.oose.dea.service.LoginServiceIMP;


@Path("login")
public class Login {


    @Inject
    private LoginService LoginService;

    public Login(){

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginrequest) {


        LoginResponseDTO responseDTO = LoginService.checkCredentialsLogin(loginrequest.getUser(),loginrequest.getPassword());

        if(responseDTO != null){
            return Response.ok(responseDTO).build();
        } else {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .build();
        }
    }

    @GET
    public String tester(){
        return "test";
}

}
