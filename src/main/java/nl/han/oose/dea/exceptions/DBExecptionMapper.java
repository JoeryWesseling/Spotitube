package nl.han.oose.dea.exceptions;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class DBExecptionMapper implements ExceptionMapper<DatabaseException> {
    @Override
    public Response toResponse(DatabaseException e) {
        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
