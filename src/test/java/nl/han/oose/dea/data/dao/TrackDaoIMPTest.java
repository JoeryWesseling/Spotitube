package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.exceptions.DatabaseException;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.TrackMapper;
import nl.han.oose.dea.data.queries.TrackQueries;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackDaoIMPTest {

    private TrackDaoIMP trackDaoIMP;

    @Mock
    private TrackMapper trackMapper;

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        trackDaoIMP = new TrackDaoIMP();

        Field dbConnField = TrackDaoIMP.class.getDeclaredField("databaseConnection");
        dbConnField.setAccessible(true);
        dbConnField.set(trackDaoIMP, databaseConnection);

        Field mapperField = TrackDaoIMP.class.getDeclaredField("trackMapper");
        mapperField.setAccessible(true);
        mapperField.set(trackDaoIMP, trackMapper);

        when(databaseConnection.getConnection()).thenReturn(connection);
    }

    @Test
    void testGetAllTracksSuccess() throws Exception {
        int playlistId = 1;
        when(connection.prepareStatement(TrackQueries.GET_ALL_TRACKS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);

        TrackDTO dto1 = new TrackDTO(1, "Song 1", "Artist 1", 200, "Album 1");
        TrackDTO dto2 = new TrackDTO(2, "Song 2", "Artist 2", 250, "Album 2");
        when(trackMapper.mapToDTO(resultSet))
                .thenReturn(dto1)
                .thenReturn(dto2);

        // Act
        List<TrackDTO> tracks = trackDaoIMP.getAllTracks(playlistId);

        // Assert
        assertNotNull(tracks);
        assertEquals(2, tracks.size());
        assertEquals("Song 1", tracks.get(0).getTitle());
        assertEquals("Song 2", tracks.get(1).getTitle());

        verify(preparedStatement).setInt(1, playlistId);
        verify(preparedStatement).executeQuery();
        verify(resultSet, atLeast(1)).next();
    }

    @Test
    void testGetAllTracksNoIdSuccess() throws Exception {
        when(connection.prepareStatement(TrackQueries.GET_ALL_TRACKS_NO_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, false);

        TrackDTO dto = new TrackDTO(3, "Song No ID", "Artist 3", 180, "Album 3");
        when(trackMapper.mapToDTO(resultSet)).thenReturn(dto);

        // Act
        List<TrackDTO> tracks = trackDaoIMP.getAllTracksNoId();

        // Assert
        assertNotNull(tracks);
        assertEquals(1, tracks.size());
        assertEquals("Song No ID", tracks.get(0).getTitle());
    }

    @Test
    void testAddTrackToPlaylistSuccess() throws Exception {
        int playlistId = 1;
        int trackId = 10;
        boolean offlineAvailable = true;

        when(connection.prepareStatement(TrackQueries.ADD_TRACK_TO_PLAYLIST)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = trackDaoIMP.addTrackToPlaylist(playlistId, trackId, offlineAvailable);

        // Assert
        assertTrue(result);
        verify(preparedStatement).setInt(1, playlistId);
        verify(preparedStatement).setInt(2, trackId);
        verify(preparedStatement).setBoolean(3, offlineAvailable);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetTrackByIdFound() throws Exception {
        int trackId = 5;
        when(connection.prepareStatement(TrackQueries.GET_TRACK_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        TrackDTO dto = new TrackDTO(trackId, "Found Song", "Found Artist", 220, "Found Album");
        when(trackMapper.mapToDTO(resultSet)).thenReturn(dto);

        // Act
        TrackDTO result = trackDaoIMP.getTrackById(trackId);

        // Assert
        assertNotNull(result);
        assertEquals("Found Song", result.getTitle());
        verify(preparedStatement).setInt(1, trackId);
    }

    @Test
    void testGetTrackByIdNotFound() throws Exception {
        int trackId = 5;
        when(connection.prepareStatement(TrackQueries.GET_TRACK_BY_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        TrackDTO result = trackDaoIMP.getTrackById(trackId);

        // Assert
        assertNull(result);
        verify(preparedStatement).setInt(1, trackId);
    }

    @Test
    void testGetAvailableTracksSuccess() throws Exception {
        int playlistId = 2;
        when(connection.prepareStatement(TrackQueries.GET_ALL_TRACKS_NOT_INPLAYLIST)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);
        TrackDTO dto1 = new TrackDTO(6, "Available Song 1", "Artist 1", 210, "Album 1");
        TrackDTO dto2 = new TrackDTO(7, "Available Song 2", "Artist 2", 230, "Album 2");
        when(trackMapper.mapToDTO(resultSet))
                .thenReturn(dto1)
                .thenReturn(dto2);

        // Act
        List<TrackDTO> tracks = trackDaoIMP.getAvailableTracks(playlistId);

        // Assert
        assertNotNull(tracks);
        assertEquals(2, tracks.size());
        verify(preparedStatement).setInt(1, playlistId);
    }

    @Test
    void testRemoveTrackFromPlaylistSuccess() throws Exception {
        int playlistId = 1;
        int trackId = 8;
        when(connection.prepareStatement(TrackQueries.REMOVE_TRACK_FROM_PLAYLIST)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        trackDaoIMP.removeTrackFromPlaylist(playlistId, trackId);
        //assert
        verify(preparedStatement).setInt(1, playlistId);
        verify(preparedStatement).setInt(2, trackId);
        verify(preparedStatement).executeUpdate();
    }

    @Test
    void testGetAllTracksThrowsDatabaseException() throws Exception {
        int playlistId = 1;
        when(connection.prepareStatement(TrackQueries.GET_ALL_TRACKS)).thenThrow(new SQLException("DB error"));

        DatabaseException ex = assertThrows(DatabaseException.class, () -> trackDaoIMP.getAllTracks(playlistId));
        assertTrue(ex.getMessage().contains("Fout bij het ophalen van de tracks"));
    }
}
