package nl.han.oose.dea.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.dto.TrackResponseDTO;
import nl.han.oose.dea.service.TokenServiceIMP;
import nl.han.oose.dea.service.TrackServiceIMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackResourceTest {

    @Mock
    private TokenServiceIMP tokenServiceIMP;

    @Mock
    private TrackServiceIMP trackServiceIMP;

    @InjectMocks
    private TrackResource trackResource;

    private final String validToken = "valid-token";
    private final String currentUser = "Frodo";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(tokenServiceIMP.isValidToken(validToken)).thenReturn(true);
        when(tokenServiceIMP.getUsernameFromToken(validToken)).thenReturn(currentUser);
    }

    @Test
    public void testGetAvailableTracks_AllTracks() {
        List<TrackDTO> mockTracks = List.of(
                new TrackDTO(1, "Song A", "Artist A", 200, "Album A"),
                new TrackDTO(2, "Song B", "Artist B", 250, "Album B")
        );
        when(trackServiceIMP.getAllTracks()).thenReturn(mockTracks);

        Response response = trackResource.getAvailableTracks(null, validToken);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO responseDTO = (TrackResponseDTO) response.getEntity();
        assertNotNull(responseDTO);
        assertEquals(2, responseDTO.getTracks().size());
    }

    @Test
    public void testGetAvailableTracks_ForPlaylist() {
        int playlistId = 1;
        List<TrackDTO> availableTracks = List.of(
                new TrackDTO(3, "Song C", "Artist C", 180, "Album C")
        );
        when(trackServiceIMP.getAvailableTracks(playlistId)).thenReturn(availableTracks);

        Response response = trackResource.getAvailableTracks(playlistId, validToken);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        TrackResponseDTO responseDTO = (TrackResponseDTO) response.getEntity();
        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.getTracks().size());
        assertEquals("Song C", responseDTO.getTracks().get(0).getTitle());
    }

    @Test
    public void testGetAvailableTracks_InvalidToken() {

        WebApplicationException exception = assertThrows(WebApplicationException.class, () -> {
            trackResource.getAvailableTracks(null, null);
        });
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), exception.getResponse().getStatus());
    }
}
