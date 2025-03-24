package nl.han.oose.dea.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class UnauthorizedExceptionMapper implements ExceptionMapper<UnauthorizedException> {

    @Override
    public Response toResponse(UnauthorizedException ex) {
        return Response.status(Response.Status.UNAUTHORIZED)
                .entity("{\"error\": \"" + ex.getMessage() + "\"}")
                .build();
    }
}
