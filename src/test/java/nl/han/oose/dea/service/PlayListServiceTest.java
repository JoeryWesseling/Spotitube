package nl.han.oose.dea.service;

import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.data.DAO.PlaylistDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PlayListServiceTest {

    @Mock
    private PlaylistDAO playlistDAO;

    @Mock
    private TrackService trackService;

    @InjectMocks
    private PlaylistService playlistService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllPlaylistsReturnWithTracks(){
        //arrange
        PlayListDTO mockPlaylist = new PlayListDTO(1,"Mock list!", true, List.of());
        List<PlayListDTO> mockPlaylists = List.of(mockPlaylist);
        List<TrackDTO>  mockTracks = List.of(new TrackDTO(1,"MockTracks","MockArtist",300,"MockAlbum"));

        when(playlistDAO.getAllPlaylists()).thenReturn(mockPlaylists);
        when(trackService.getAllByPlaylists(1)).thenReturn(mockTracks);
        //act
        List<PlayListDTO> result = playlistService.getAllPlayLists();
        //assert
        assertEquals(1,result.size());
        assertEquals("Mock list!",result.get(0).getName());
        assertEquals(1,result.get(0).getTracks().size());
        assertEquals("MockTracks",result.get(0).getTracks().get(0).getTitle());

        verify(playlistDAO,times(1)).getAllPlaylists();
        verify(trackService,times(1)).getAllByPlaylists(1);
    }
}
