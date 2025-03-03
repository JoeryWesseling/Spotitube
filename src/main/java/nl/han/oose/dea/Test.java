package nl.han.oose.dea;


import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;

@Path("/test")
public class Test {

    @GET
    public String helloWorld(){
        return "Hello!";
    }
}
