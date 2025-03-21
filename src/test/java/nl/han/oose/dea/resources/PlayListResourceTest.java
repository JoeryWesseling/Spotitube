package nl.han.oose.dea.resources;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.data.dao.PlaylistDaoIMP;
import nl.han.oose.dea.dto.PlayListDTO;
import nl.han.oose.dea.dto.PlayListResponseDTO;
import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.service.PlaylistServiceIMP;
import nl.han.oose.dea.service.TokenServiceIMP;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlayListResourceTest {

    @Mock
    private TokenServiceIMP tokenServiceIMPMock;

    @Mock
    private PlaylistServiceIMP playlistServiceIMPMock;
    @Mock
    private PlaylistDaoIMP playlistDaoIMP;

    @InjectMocks
    private PlaylistResource playlistResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetPlayListWithValidToken() {
        //Arrange
        String validToken = "Sauron";

        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);

        List<TrackDTO> tracks = List.of(new TrackDTO(1, "song", "artist", 200, "album"));
        List<PlayListDTO> mockPlaylists = List.of(new PlayListDTO(1, "playlist", true, tracks));
        when(playlistServiceIMPMock.getAllPlayLists()).thenReturn(mockPlaylists);

        //act
        Response response = playlistResource.getPlayLists(validToken);

        //Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertEquals(1, responseDTO.getPlaylists().size());
        assertEquals(200, responseDTO.getLength());
    }


    @Test
    void testGetPlaylistWithInvalidToken() {
        // Arrange
        String invalidToken = "NotSauron";
        when(tokenServiceIMPMock.isValidToken(invalidToken)).thenReturn(false);

        // Act & Assert
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            playlistResource.getPlayLists(invalidToken);
        });
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), ex.getResponse().getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", ex.getResponse().getEntity());
    }

    @Test
    void testGetPlaylistsWithNoTracks() {
        //arrange
        String validToken = "Sauron";
        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);

        List<PlayListDTO> emptyList = List.of(new PlayListDTO(1, "empty", true, Collections.emptyList()));
        when(playlistServiceIMPMock.getAllPlayLists()).thenReturn(emptyList);

        //act
        Response response = playlistResource.getPlayLists(validToken);

        //assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertEquals(0, responseDTO.getLength());
    }

    @Test
    void testGetPlaylistWithNullToken() {
        // Act & Assert
        WebApplicationException ex = assertThrows(WebApplicationException.class, () -> {
            playlistResource.getPlayLists(null);
        });
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), ex.getResponse().getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", ex.getResponse().getEntity());
    }

    @Test
    void testUpdatePlaylistNameSucces() {
        String validToken = "Sauron";
        int playlistId = 1;

        PlayListDTO updatedPlaylist = new PlayListDTO(playlistId, "New Playlist Name", true,
                List.of(new TrackDTO(1, "song", "artist", 200, "album")));

        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);
        when(playlistServiceIMPMock.updatePlaylistName(playlistId, updatedPlaylist.getName(), validToken)).thenReturn(true);

        when(playlistServiceIMPMock.getAllPlayLists()).thenReturn(List.of(updatedPlaylist));
        when(playlistDaoIMP.isOwner(playlistId, "Sauron")).thenReturn(true);

        Response response = playlistResource.updatePlaylistName(playlistId, validToken, updatedPlaylist);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.getPlaylists().size());
        assertEquals("New Playlist Name", responseDTO.getPlaylists().get(0).getName());
    }

    @Test
    void testUpdatePlaylistNameFailure() {

        String validToken = "Sauron";
        int playlistId = 1;
        PlayListDTO updatedPlaylist = new PlayListDTO(playlistId, "New Playlist Name", true, List.of());

        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);
        when(playlistServiceIMPMock.updatePlaylistName(playlistId, updatedPlaylist.getName(), validToken)).thenReturn(false);

        Response response = playlistResource.updatePlaylistName(playlistId, validToken, updatedPlaylist);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Playlist not found or you are not the owner\"}", response.getEntity());
    }

    @Test
    void testAddPlaylistSucces() {
        String validToken = "Sauron";
        PlayListDTO newList = new PlayListDTO(2, "Brand New Playlist", true, List.of());

        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);
        when(playlistServiceIMPMock.getAllPlayLists()).thenReturn(List.of(newList));
        when(playlistDaoIMP.isOwner(newList.getId(), "Sauron")).thenReturn(true);

        Response response = playlistResource.addPlaylist(validToken, newList);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertNotNull(responseDTO);
        assertEquals(1,responseDTO.getPlaylists().size());
    }

    @Test
    void testDeletePlaylistSuccess() {
        // Arrange
        String validToken = "Sauron";
        int playlistId = 1;

        // Stub token validation and username retrieval
        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);
        when(tokenServiceIMPMock.getUsernameFromToken(validToken)).thenReturn("Sauron");

        // Simulate a successful deletion
        when(playlistServiceIMPMock.deletePlaylist(playlistId, "Sauron")).thenReturn(true);
        // After deletion, assume a remaining playlist is returned
        PlayListDTO remainingPlaylist = new PlayListDTO(2, "Remaining Playlist", true, List.of());
        when(playlistServiceIMPMock.getAllPlayLists()).thenReturn(List.of(remainingPlaylist));
        when(playlistDaoIMP.isOwner(remainingPlaylist.getId(), "Sauron")).thenReturn(true);

        // Act
        Response response = playlistResource.deletePlaylist(playlistId, validToken);

        // Assert
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertNotNull(responseDTO);
        assertEquals(1, responseDTO.getPlaylists().size());
    }


    @Test
    void testDeletePlaylistFailure() {
        // Arrange
        String validToken = "Sauron";
        int playlistId = 1;

        when(tokenServiceIMPMock.isValidToken(validToken)).thenReturn(true);
        when(playlistServiceIMPMock.deletePlaylist(playlistId, "Sauron")).thenReturn(false);

        // Act
        Response response = playlistResource.deletePlaylist(playlistId, validToken);

        // Assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(), response.getStatus());
        assertEquals("{\"error\": \"Invalid or missing token\"}", response.getEntity());
    }
}

