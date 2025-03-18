package nl.han.oose.dea.service;

import nl.han.oose.dea.data.dao.TrackDAO;
import nl.han.oose.dea.dto.TrackDTO;
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
                new TrackDTO(1, "HUMBLE.", "Kendrick Lamar", 177, "DAMN."),
                new TrackDTO(2, "Alright", "Kendrick Lamar", 212, "To Pimp a Butterfly")
        );

        when(trackDAO.getAllTracks(1)).thenReturn(mockTracks);

        // Act
        List<TrackDTO> result = trackService.getAllByPlaylists(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("HUMBLE.", result.get(0).getTitle());
        assertEquals("Alright", result.get(1).getTitle());
        verify(trackDAO, times(1)).getAllTracks(1);
    }

    @Test
    void getAllByPlaylistsReturnsEmptyList() {
        // Arrange
        when(trackDAO.getAllTracks(999)).thenReturn(Collections.emptyList());

        // Act
        List<TrackDTO> result = trackService.getAllByPlaylists(999);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trackDAO, times(1)).getAllTracks(999);
    }

    @Test
    void getAllByPlaylistsHandlesNullGracefully() {
        // Arrange
        when(trackDAO.getAllTracks(1)).thenReturn(null);

        // Act
        List<TrackDTO> result = trackService.getAllByPlaylists(1);

        // Assert
        assertNull(result);
        verify(trackDAO, times(1)).getAllTracks(1);
    }

    @Test
    void testAddTrackSucces() {
        // Arrange
        int playlistId = 1;
        int trackId = 4;
        boolean offlineAvailable = true;
        when(trackDAO.addTrackToPlaylist(playlistId, trackId, offlineAvailable)).thenReturn(true);

        // Act
        boolean result = trackService.addTrackToPlaylist(playlistId, trackId, offlineAvailable);

        // Assert
        assertTrue(result, "Expected adding track to return true");
        verify(trackDAO, times(1)).addTrackToPlaylist(playlistId, trackId, offlineAvailable);
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

    // --- New tests for additional methods ---

    @Test
    void testGetAvailableTracksReturnsList() {
        // Arrange
        List<TrackDTO> availableTracks = List.of(
                new TrackDTO(5, "Track A", "Artist A", 200, "Album A"),
                new TrackDTO(6, "Track B", "Artist B", 220, "Album B")
        );
        when(trackDAO.getAvailableTracks(1)).thenReturn(availableTracks);

        // Act
        List<TrackDTO> result = trackService.getAvailableTracks(1);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Track A", result.get(0).getTitle());
        assertEquals("Track B", result.get(1).getTitle());
        verify(trackDAO, times(1)).getAvailableTracks(1);
    }

    @Test
    void testGetAvailableTracksReturnsEmptyList() {
        // Arrange
        when(trackDAO.getAvailableTracks(1)).thenReturn(Collections.emptyList());

        // Act
        List<TrackDTO> result = trackService.getAvailableTracks(1);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trackDAO, times(1)).getAvailableTracks(1);
    }

    @Test
    void testGetAllTracksNoIdReturnsList() {
        // Arrange
        List<TrackDTO> allTracks = List.of(
                new TrackDTO(7, "Track X", "Artist X", 180, "Album X"),
                new TrackDTO(8, "Track Y", "Artist Y", 240, "Album Y")
        );
        when(trackDAO.getAllTracksNoId()).thenReturn(allTracks);

        // Act
        List<TrackDTO> result = trackService.getAllTracks();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Track X", result.get(0).getTitle());
        assertEquals("Track Y", result.get(1).getTitle());
        verify(trackDAO, times(1)).getAllTracksNoId();
    }

    @Test
    void testGetAllTracksNoIdReturnsEmptyList() {
        // Arrange
        when(trackDAO.getAllTracksNoId()).thenReturn(Collections.emptyList());
        // Act
        List<TrackDTO> result = trackService.getAllTracks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(trackDAO, times(1)).getAllTracksNoId();
    }

    @Test
    void testRemoveTrackFromPlaylist() {
        // Arrange
        int playlistId = 1;
        int trackId = 2;
        // For void methods, use doNothing()
        doNothing().when(trackDAO).removeTrackFromPlaylist(playlistId, trackId);

        // Act
        trackService.removeTrackFromPlaylist(playlistId, trackId);

        // Assert: Verify that the DAO method was invoked exactly once
        verify(trackDAO, times(1)).removeTrackFromPlaylist(playlistId, trackId);
    }
}
