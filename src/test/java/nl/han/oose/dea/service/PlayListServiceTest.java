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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class PlayListServiceTest {

    @Mock
    private PlaylistDAO playlistDAO;

    @Mock
    private TrackService trackService;

    @Mock
    private TokenService tokenService;

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

    @Test
    void testUpdatePlaylistName_Successful() {
        // Arrange
        int playlistId = 1;
        String newName = "My Updated Playlist";
        String token = "valid-token";
        String username = "Frodo";

        when(tokenService.getUsernameFromToken(token)).thenReturn(username);
        when(playlistDAO.isOwner(playlistId, username)).thenReturn(true);
        when(playlistDAO.updatePlaylistName(playlistId, newName)).thenReturn(true);

        // Act
        boolean result = playlistService.updatePlaylistName(playlistId, newName, token);

        // Assert
        assertTrue(result);
        verify(playlistDAO, times(1)).updatePlaylistName(playlistId, newName);
    }

    @Test
    void testAddPlaylist() {
        // Arrange
        PlayListDTO newPlaylist = new PlayListDTO(-1, "Progressive Rock", false, List.of());
        when(playlistDAO.getNextPlaylistId()).thenReturn(3);
        doNothing().when(playlistDAO).addPlaylist(any(PlayListDTO.class), anyString());

        // Act
        playlistService.addPlaylist(newPlaylist, "Frodo");

        // Assert
        assertEquals(3, newPlaylist.getId());
        assertTrue(newPlaylist.isOwner());
        verify(playlistDAO, times(1)).addPlaylist(any(PlayListDTO.class), eq("Frodo"));
    }


}
