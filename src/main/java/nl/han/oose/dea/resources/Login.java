package nl.han.oose.dea.resources;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.LoginRequestDTO;
import nl.han.oose.dea.DTO.LoginResponseDTO;
import nl.han.oose.dea.service.LoginService;


@Path("login")
public class Login {

    private  LoginService loginService = new LoginService();

    public Login(){

    }

    @Inject
    public Login(LoginService loginService){
        this.loginService = loginService;
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response login(LoginRequestDTO loginrequest) {


        LoginResponseDTO responseDTO = loginService.checkCredentialsLogin(loginrequest.getUser(),loginrequest.getPassword());

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
