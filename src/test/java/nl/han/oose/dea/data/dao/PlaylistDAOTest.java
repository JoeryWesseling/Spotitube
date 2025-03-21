package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.dto.PlayListDTO;
import nl.han.oose.dea.exceptions.DatabaseException;
import nl.han.oose.dea.data.mappers.PlaylistMapper;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.queries.PlaylistQueries;
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

public class PlaylistDAOTest {

    private PlaylistDAO playlistDAO;

    @Mock
    private DatabaseConnection databaseConnection;

    @Mock
    private PlaylistMapper playlistMapper;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        playlistDAO = new PlaylistDAO();

        Field dbConnField = PlaylistDAO.class.getDeclaredField("databaseConnection");
        dbConnField.setAccessible(true);
        dbConnField.set(playlistDAO, databaseConnection);

        Field mapperField = PlaylistDAO.class.getDeclaredField("playlistMapper");
        mapperField.setAccessible(true);
        mapperField.set(playlistDAO, playlistMapper);

        when(databaseConnection.getConnection()).thenReturn(connection);
    }

    @Test
    void testGetAllPlaylistsSuccess() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.GET_ALL_PLAYLISTS)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true, true, false);

        PlayListDTO dto1 = new PlayListDTO(1, "Playlist 1", true, List.of());
        PlayListDTO dto2 = new PlayListDTO(2, "Playlist 2", false, List.of());
        when(playlistMapper.mapToDTO(resultSet))
                .thenReturn(dto1)
                .thenReturn(dto2);

        // Act
        List<PlayListDTO> playlists = playlistDAO.getAllPlaylists();

        // Assert
        assertNotNull(playlists);
        assertEquals(2, playlists.size());
        assertEquals("Playlist 1", playlists.get(0).getName());
        assertEquals("Playlist 2", playlists.get(1).getName());

        verify(resultSet).close();
        verify(preparedStatement).close();
        verify(connection).close();
    }

    @Test
    void testGetAllPlaylistsThrowsDatabaseException() throws Exception {
        // Arrange:
        when(connection.prepareStatement(PlaylistQueries.GET_ALL_PLAYLISTS))
                .thenThrow(new SQLException("DB error"));

        // Act & Assert
        DatabaseException ex = assertThrows(DatabaseException.class, () -> playlistDAO.getAllPlaylists());
        assertTrue(ex.getMessage().contains("Fout bij het ophalen playlists"));
    }

    @Test
    void testIsOwnerTrue() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.IS_OWNER)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        // Act
        boolean isOwner = playlistDAO.isOwner(1, "user");

        // Assert
        assertTrue(isOwner);
    }

    @Test
    void testIsOwnerFalse() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.IS_OWNER)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(0);

        // Act
        boolean isOwner = playlistDAO.isOwner(1, "user");

        // Assert
        assertFalse(isOwner);
    }

    @Test
    void testUpdatePlaylistNameSuccess() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.UPDATE_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = playlistDAO.updatePlaylistName(1, "New Name");

        // Assert
        assertTrue(result);
    }

    @Test
    void testUpdatePlaylistNameFailure() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.UPDATE_NAME)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = playlistDAO.updatePlaylistName(1, "New Name");

        // Assert
        assertFalse(result);
    }

    @Test
    void testGetNextPlaylistIdSuccess() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.GET_NEXT_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(5);

        // Act
        int nextId = playlistDAO.getNextPlaylistId();

        // Assert
        assertEquals(6, nextId);
    }

    @Test
    void testGetNextPlaylistIdEmptyResult() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.GET_NEXT_ID)).thenReturn(preparedStatement);
        when(preparedStatement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        // Act
        int nextId = playlistDAO.getNextPlaylistId();

        // Asseert
        assertEquals(1, nextId);
    }

    @Test
    void testAddPlaylistSuccess() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.ADD_PLAYLIST)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        PlayListDTO newList = new PlayListDTO(10, "New Playlist", true, List.of());

        // Act
        playlistDAO.addPlaylist(newList, "user");

        // Assert
        verify(preparedStatement).setInt(1, newList.getId());
        verify(preparedStatement).setString(2, newList.getName());
        verify(preparedStatement).setString(3, "user");
        verify(preparedStatement).setBoolean(4, true);
        verify(preparedStatement).executeUpdate();
    }


    @Test
    void testDeletePlaylistSuccess() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.DELETE_PLAYLIST)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(1);

        // Act
        boolean result = playlistDAO.deletePlaylist(1);

        // Assert
        assertTrue(result);
    }

    @Test
    void testDeletePlaylistFailure() throws Exception {
        // Arrange
        when(connection.prepareStatement(PlaylistQueries.DELETE_PLAYLIST)).thenReturn(preparedStatement);
        when(preparedStatement.executeUpdate()).thenReturn(0);

        // Act
        boolean result = playlistDAO.deletePlaylist(1);

        // Assert
        assertFalse(result);
    }
}
