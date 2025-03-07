package nl.han.oose.dea.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.DTO.TrackResponseDTO;
import nl.han.oose.dea.service.TokenService;
import nl.han.oose.dea.service.TrackService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class TrackResourceTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private TrackResource trackResource;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testTracksReturnsUnauthorizedWhenTokenIsInvalid() {
        //arrange
        String invalidToken = "invalidToken";
        int playlistId = 1;
        when(tokenService.isValidToken(invalidToken)).thenReturn(false);

        //act
        Response response = trackResource.tracks(playlistId, invalidToken);

        //assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"invalid or missing token\"}", response.getEntity());
        verify(tokenService, times(1)).isValidToken(invalidToken);
        verify(trackService, never()).getAllByPlaylists(anyInt());
    }

    @Test
    void testTracksReturnWhenTokenValid() {
        //arrange
        String validToken = "validToken";
        int playlistId = 2;
        List<TrackDTO> mockTracks = List.of(
                new TrackDTO(1, "track1", "artist1", 200, "album1"),
                new TrackDTO(2, "track2", "artist2", 200, "album2")
        );
        when(tokenService.isValidToken(validToken)).thenReturn(true);
        when(trackService.getAllByPlaylists(playlistId)).thenReturn(mockTracks);
        //act
        Response response = trackResource.tracks(playlistId, validToken);
        TrackResponseDTO trackResponseDTO = (TrackResponseDTO) response.getEntity();

        //assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertNotNull(trackResponseDTO);
        assertEquals(2, trackResponseDTO.getTracks().size());
        assertEquals("track1", trackResponseDTO.getTracks().get(0).getTitle());
        assertEquals("track2", trackResponseDTO.getTracks().get(1).getTitle());

        verify(tokenService, times(1)).isValidToken(validToken);
        verify(trackService, times(1)).getAllByPlaylists(playlistId);
    }
}
