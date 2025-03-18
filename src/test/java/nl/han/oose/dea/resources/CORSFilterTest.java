package nl.han.oose.dea.resources;

import jakarta.ws.rs.container.ContainerRequestContext;
import jakarta.ws.rs.container.ContainerResponseContext;
import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CORSFilterTest {

    private CORSFilter corsFilter;
    private ContainerRequestContext requestContext;
    private ContainerResponseContext responseContext;
    private MultivaluedMap<String, Object> headers;

    @BeforeEach
    void setUp() {
        corsFilter = new CORSFilter();
        // Create a mock for the request context.
        requestContext = mock(ContainerRequestContext.class);
        // Create a mock for the response context.
        responseContext = mock(ContainerResponseContext.class);
        // Use a real MultivaluedMap for headers.
        headers = new MultivaluedHashMap<>();
        when(responseContext.getHeaders()).thenReturn(headers);
    }

    @Test
    void testCORSFilterAddsHeaders() {
        // Act: call the filter method.
        corsFilter.filter(requestContext, responseContext);

        // Assert: verify that the expected CORS headers have been added.
        assertEquals("*", headers.getFirst("Access-Control-Allow-Origin"));
        assertEquals("origin, content-type, accept, authorization", headers.getFirst("Access-Control-Allow-Headers"));
        assertEquals("true", headers.getFirst("Access-Control-Allow-Credentials"));
        assertEquals("GET, POST, PUT, DELETE, OPTIONS, HEAD", headers.getFirst("Access-Control-Allow-Methods"));
        assertEquals("1209600", headers.getFirst("Access-Control-Max-Age"));
    }
}
