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
import nl.han.oose.dea.data.dao.PlaylistDaoIMP;
import nl.han.oose.dea.service.TrackServiceIMP;

public class PlaylistTracksResourceTest {

    private TrackServiceIMP trackServiceIMP;
    private PlaylistDaoIMP playlistDaoIMP;
    private PlaylistTracksResource resource;

    @BeforeEach
    public void setup() throws Exception {
        trackServiceIMP = mock(TrackServiceIMP.class);
        playlistDaoIMP = mock(PlaylistDaoIMP.class);

        resource = Mockito.spy(new PlaylistTracksResource());

        Field trackServiceField = PlaylistTracksResource.class.getDeclaredField("trackService");
        trackServiceField.setAccessible(true);
        trackServiceField.set(resource, trackServiceIMP);

        Field playlistDAOField = PlaylistTracksResource.class.getDeclaredField("playlistDAO");
        playlistDAOField.setAccessible(true);
        playlistDAOField.set(resource, playlistDaoIMP);

        doReturn("testUser").when(resource).authenticate(anyString());
    }

    @Test
    public void testGetTracksSuccess() {
        int playlistId = 1;
        String token = "validToken";
        List<TrackDTO> tracks = Arrays.asList(new TrackDTO(), new TrackDTO());
        when(trackServiceIMP.getAllByPlaylists(playlistId)).thenReturn(tracks);

        Response response = resource.tracks(playlistId, token);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO trackResponse = (TrackResponseDTO) response.getEntity();
        assertNotNull(trackResponse);
        assertEquals(tracks, trackResponse.getTracks());
        verify(trackServiceIMP).getAllByPlaylists(playlistId);
    }

    @Test
    public void testAddTrackToPlaylistSuccess() {
        int playlistId = 1;
        String token = "validToken";
        TrackDTO trackRequest = new TrackDTO();
        trackRequest.setId(100);
        trackRequest.setOfflineAvailable(true);

        when(playlistDaoIMP.isOwner(playlistId, "testUser")).thenReturn(true);
        when(trackServiceIMP.addTrackToPlaylist(playlistId, 100, true)).thenReturn(true);
        List<TrackDTO> updatedTracks = Arrays.asList(trackRequest);
        when(trackServiceIMP.getAllByPlaylists(playlistId)).thenReturn(updatedTracks);

        Response response = resource.addTrackToPlaylist(playlistId, token, trackRequest);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO trackResponse = (TrackResponseDTO) response.getEntity();
        assertNotNull(trackResponse);
        assertEquals(updatedTracks, trackResponse.getTracks());

        verify(playlistDaoIMP).isOwner(playlistId, "testUser");
        verify(trackServiceIMP).addTrackToPlaylist(playlistId, 100, true);
        verify(trackServiceIMP).getAllByPlaylists(playlistId);
    }

    @Test
    public void testAddTrackToPlaylistNotOwner() {
        int playlistId = 1;
        String token = "validToken";
        TrackDTO trackRequest = new TrackDTO();
        trackRequest.setId(100);
        trackRequest.setOfflineAvailable(true);

        when(playlistDaoIMP.isOwner(playlistId, "testUser")).thenReturn(false);

        Response response = resource.addTrackToPlaylist(playlistId, token, trackRequest);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());
        verify(playlistDaoIMP).isOwner(playlistId, "testUser");
        verify(trackServiceIMP, never()).addTrackToPlaylist(anyInt(), anyInt(), anyBoolean());
    }

    @Test
    public void testAddTrackToPlaylistAddFailure() {
        int playlistId = 1;
        String token = "validToken";
        TrackDTO trackRequest = new TrackDTO();
        trackRequest.setId(100);
        trackRequest.setOfflineAvailable(true);

        when(playlistDaoIMP.isOwner(playlistId, "testUser")).thenReturn(true);
        when(trackServiceIMP.addTrackToPlaylist(playlistId, 100, true)).thenReturn(false);

        Response response = resource.addTrackToPlaylist(playlistId, token, trackRequest);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());
        verify(playlistDaoIMP).isOwner(playlistId, "testUser");
        verify(trackServiceIMP).addTrackToPlaylist(playlistId, 100, true);
    }

    @Test
    public void testRemoveTrackFromPlaylistSuccess() {
        int playlistId = 1;
        int trackId = 200;
        String token = "validToken";

        when(playlistDaoIMP.isOwner(playlistId, "testUser")).thenReturn(true);
        doNothing().when(trackServiceIMP).removeTrackFromPlaylist(playlistId, trackId);
        List<TrackDTO> updatedTracks = Arrays.asList(new TrackDTO());
        when(trackServiceIMP.getAllByPlaylists(playlistId)).thenReturn(updatedTracks);

        Response response = resource.removeTrackFromPlaylist(playlistId, token, trackId);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        TrackResponseDTO trackResponse = (TrackResponseDTO) response.getEntity();
        assertNotNull(trackResponse);
        assertEquals(updatedTracks, trackResponse.getTracks());

        verify(playlistDaoIMP).isOwner(playlistId, "testUser");
        verify(trackServiceIMP).removeTrackFromPlaylist(playlistId, trackId);
        verify(trackServiceIMP).getAllByPlaylists(playlistId);
    }

    @Test
    public void testRemoveTrackFromPlaylistNotOwner() {
        int playlistId = 1;
        int trackId = 200;
        String token = "validToken";

        when(playlistDaoIMP.isOwner(playlistId, "testUser")).thenReturn(false);

        Response response = resource.removeTrackFromPlaylist(playlistId, token, trackId);

        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());

        verify(playlistDaoIMP).isOwner(playlistId, "testUser");
        verify(trackServiceIMP, never()).removeTrackFromPlaylist(anyInt(), anyInt());
    }
}
