package nl.han.oose.dea.resources;

import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.PlayListResponseDTO;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.service.PlaylistService;
import nl.han.oose.dea.service.TokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayListResourceTest {

    @Mock
    private TokenService tokenServiceMock;

    @Mock
    private PlaylistService playlistServiceMock;

    @InjectMocks
    private PlaylistResource playlistResource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testGetPlayListWithValidToken(){
        //Arrange
        String validToken = "Sauron";

        when(tokenServiceMock.isValidToken(validToken)).thenReturn(true);

        List<TrackDTO> tracks = List.of(new TrackDTO(1,"song","artist",200,"album"));
        List<PlayListDTO> mockPlaylists = List.of(new PlayListDTO(1,"playlist",true,tracks));
        when(playlistServiceMock.getAllPlayLists()).thenReturn(mockPlaylists);

        //act
        Response response = playlistResource.getPlayLists(validToken);

        //Assert
        assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertEquals(1,responseDTO.getPlaylists().size());
        assertEquals(200,responseDTO.getLength());
    }


    @Test
    void testGetPlaylistWithInvalidToken(){

        //arrange
        String invalidToken = "NotSauron";
        when(tokenServiceMock.isValidToken(invalidToken)).thenReturn(false);

        //act
        Response response = playlistResource.getPlayLists(invalidToken);

        //assert
        assertEquals(Response.Status.UNAUTHORIZED.getStatusCode(),response.getStatus());
    }

    @Test
    void testGetPlaylistsWithNoTracks(){
        //arrange
        String validToken = "Sauron";
        when(tokenServiceMock.isValidToken(validToken)).thenReturn(true);

        List<PlayListDTO> emptyList = List.of(new PlayListDTO(1,"empty",true, Collections.emptyList()));
        when(playlistServiceMock.getAllPlayLists()).thenReturn(emptyList);

        //act
        Response response = playlistResource.getPlayLists(validToken);

        //assert
        assertEquals(Response.Status.OK.getStatusCode(),response.getStatus());
        PlayListResponseDTO responseDTO = (PlayListResponseDTO) response.getEntity();
        assertEquals(0,responseDTO.getLength());
    }
}

