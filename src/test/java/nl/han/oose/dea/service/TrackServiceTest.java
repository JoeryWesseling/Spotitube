package nl.han.oose.dea.service;

import nl.han.oose.dea.data.DAO.TrackDAO;
import nl.han.oose.dea.DTO.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackServiceTest {

    @Mock
    private TrackDAO trackDAO;

    @InjectMocks
    private TrackService trackService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllByPlaylistsReturnsTrackForValidPlaylists() {
        // Arrange
        List<TrackDTO> mockTracks = List.of(
                new TrackDTO(1,"HUMBLE.","Kendrick Lamar",177,"DAMN."),
                new TrackDTO(2,"Alright","Kendrick Lamar",212,"To Pimp a Butterfly")
                );

        when(trackDAO.getAllTracks(1)).thenReturn(mockTracks);

        // Act
        List<TrackDTO> result = trackService.getAllByPlaylists(1);

        // Assert
        assertNotNull(result);
        assertEquals(2,result.size());
        assertEquals("HUMBLE.",result.get(0).getTitle());
        assertEquals("Alright",result.get(1).getTitle());

        verify(trackDAO,times(1)).getAllTracks(1);

    }

    @Test
    void getAllByPlaylistsReturnsEmptyList(){
        //arrange
        when(trackDAO.getAllTracks(999)).thenReturn(Collections.emptyList());
        //act
        List<TrackDTO> result = trackService.getAllByPlaylists(999);
        //assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(trackDAO,times(1)).getAllTracks(999);
    }

    @Test
    void getAllPlaylistsHandlesNullGracefully(){
        //arrange
        when(trackDAO.getAllTracks(1)).thenReturn(null);
        //act
        List<TrackDTO> result = trackService.getAllByPlaylists(1);
        //assert
        assertNull(result);

        verify(trackDAO,times(1)).getAllTracks(1);
    }

    @Test
    void testAddTrackSucces(){
        //arrange
        int playlistId = 1;
        int trackId = 4;
        boolean offlineAvailable = true;
        when(trackDAO.addTrackToPlaylist(playlistId,trackId,offlineAvailable)).thenReturn(true);

        //act
        boolean result = trackService.addTrackToPlaylist(playlistId,trackId,offlineAvailable);
        //assert
        assertTrue(result,"Expected adding track to return true");
    }

    @Test
    void testAddTrackToPlaylist_Failure() {
        // Arrange
        int playlistId = 1;
        int trackId = 4;
        boolean offlineAvailable = true;
        when(trackDAO.addTrackToPlaylist(playlistId, trackId, offlineAvailable)).thenReturn(false);

        // Act
        boolean result = trackService.addTrackToPlaylist(playlistId, trackId, offlineAvailable);

        // Assert
        assertFalse(result, "Expected adding track to return false");
        verify(trackDAO, times(1)).addTrackToPlaylist(playlistId, trackId, offlineAvailable);
    }

}
