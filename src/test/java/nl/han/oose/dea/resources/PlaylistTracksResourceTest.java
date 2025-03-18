package nl.han.oose.dea.resources;

import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.dto.TrackResponseDTO;
import nl.han.oose.dea.data.dao.PlaylistDAO;
import nl.han.oose.dea.service.TrackService;

public class PlaylistTracksResourceTest {

    private TrackService trackService;
    private PlaylistDAO playlistDAO;
    private PlaylistTracksResource resource;

    @BeforeEach
    public void setup() throws Exception {
        trackService = mock(TrackService.class);
        playlistDAO = mock(PlaylistDAO.class);

        resource = Mockito.spy(new PlaylistTracksResource());

        Field trackServiceField = PlaylistTracksResource.class.getDeclaredField("trackService");
        trackServiceField.setAccessible(true);
        trackServiceField.set(resource, trackService);

        Field playlistDAOField = PlaylistTracksResource.class.getDeclaredField("playlistDAO");
        playlistDAOField.setAccessible(true);
        playlistDAOField.set(resource, playlistDAO);

        doReturn("testUser").when(resource).authenticate(anyString());
    }

    @Test
    public void testGetTracksSuccess() {
        int playlistId = 1;
        String token = "validToken";
        List<TrackDTO> tracks = Arrays.asList(new TrackDTO(), new TrackDTO());
        when(trackService.getAllByPlaylists(playlistId)).thenReturn(tracks);

        Response response = resource.tracks(playlistId, token);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO trackResponse = (TrackResponseDTO) response.getEntity();
        assertNotNull(trackResponse);
        assertEquals(tracks, trackResponse.getTracks());
        verify(trackService).getAllByPlaylists(playlistId);
    }

    @Test
    public void testAddTrackToPlaylistSuccess() {
        int playlistId = 1;
        String token = "validToken";
        TrackDTO trackRequest = new TrackDTO();
        trackRequest.setId(100);
        trackRequest.setOfflineAvailable(true);

        when(playlistDAO.isOwner(playlistId, "testUser")).thenReturn(true);
        when(trackService.addTrackToPlaylist(playlistId, 100, true)).thenReturn(true);
        List<TrackDTO> updatedTracks = Arrays.asList(trackRequest);
        when(trackService.getAllByPlaylists(playlistId)).thenReturn(updatedTracks);

        Response response = resource.addTrackToPlaylist(playlistId, token, trackRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO trackResponse = (TrackResponseDTO) response.getEntity();
        assertNotNull(trackResponse);
        assertEquals(updatedTracks, trackResponse.getTracks());

        verify(playlistDAO).isOwner(playlistId, "testUser");
        verify(trackService).addTrackToPlaylist(playlistId, 100, true);
        verify(trackService).getAllByPlaylists(playlistId);
    }

    @Test
    public void testAddTrackToPlaylistNotOwner() {
        int playlistId = 1;
        String token = "validToken";
        TrackDTO trackRequest = new TrackDTO();
        trackRequest.setId(100);
        trackRequest.setOfflineAvailable(true);

        when(playlistDAO.isOwner(playlistId, "testUser")).thenReturn(false);

        Response response = resource.addTrackToPlaylist(playlistId, token, trackRequest);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());
        verify(playlistDAO).isOwner(playlistId, "testUser");
        verify(trackService, never()).addTrackToPlaylist(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void testAddTrackToPlaylistAddFailure() {
        int playlistId = 1;
        String token = "validToken";
        TrackDTO trackRequest = new TrackDTO();
        trackRequest.setId(100);
        trackRequest.setOfflineAvailable(true);

        when(playlistDAO.isOwner(playlistId, "testUser")).thenReturn(true);
        when(trackService.addTrackToPlaylist(playlistId, 100, true)).thenReturn(false);

        Response response = resource.addTrackToPlaylist(playlistId, token, trackRequest);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());
        verify(playlistDAO).isOwner(playlistId, "testUser");
        verify(trackService).addTrackToPlaylist(playlistId, 100, true);
    }

    @Test
    public void testRemoveTrackFromPlaylistSuccess() {
        int playlistId = 1;
        int trackId = 200;
        String token = "validToken";

        when(playlistDAO.isOwner(playlistId, "testUser")).thenReturn(true);
        doNothing().when(trackService).removeTrackFromPlaylist(playlistId, trackId);
        List<TrackDTO> updatedTracks = Arrays.asList(new TrackDTO());
        when(trackService.getAllByPlaylists(playlistId)).thenReturn(updatedTracks);

        Response response = resource.removeTrackFromPlaylist(playlistId, token, trackId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO trackResponse = (TrackResponseDTO) response.getEntity();
        assertNotNull(trackResponse);
        assertEquals(updatedTracks, trackResponse.getTracks());

        verify(playlistDAO).isOwner(playlistId, "testUser");
        verify(trackService).removeTrackFromPlaylist(playlistId, trackId);
        verify(trackService).getAllByPlaylists(playlistId);
    }

    @Test
    public void testRemoveTrackFromPlaylistNotOwner() {
        int playlistId = 1;
        int trackId = 200;
        String token = "validToken";

        when(playlistDAO.isOwner(playlistId, "testUser")).thenReturn(false);

        Response response = resource.removeTrackFromPlaylist(playlistId, token, trackId);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());

        verify(playlistDAO).isOwner(playlistId, "testUser");
        verify(trackService, never()).removeTrackFromPlaylist(anyInt(), anyInt());
    }
}
