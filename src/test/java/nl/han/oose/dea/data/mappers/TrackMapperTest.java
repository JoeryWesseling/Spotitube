package nl.han.oose.dea.data.mappers;

import nl.han.oose.dea.dto.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TrackMapperTest {

    private TrackMapper trackMapper;
    private ResultSet mockResultSet;

    @BeforeEach
    void setup() {
        trackMapper = new TrackMapper();
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void testMapToDTO_WithValidResultSet() throws SQLException {
        // Arrange
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("title")).thenReturn("HUMBLE.");
        when(mockResultSet.getString("artist")).thenReturn("Kendrick Lamar");
        when(mockResultSet.getInt("duration")).thenReturn(177);
        when(mockResultSet.getString("album")).thenReturn("DAMN.");

        // Act
        TrackDTO result = trackMapper.mapToDTO(mockResultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("HUMBLE.", result.getTitle());
        assertEquals("Kendrick Lamar", result.getPerformer());
        assertEquals(177, result.getDuration());
        assertEquals("DAMN.", result.getAlbum());

        // Verify interactions with ResultSet
        verify(mockResultSet, times(1)).getInt("id");
        verify(mockResultSet, times(1)).getString("title");
        verify(mockResultSet, times(1)).getString("artist");
        verify(mockResultSet, times(1)).getInt("duration");
        verify(mockResultSet, times(1)).getString("album");
    }


    @Test
    void testMapToDTO_WithEmptyResultSet() throws SQLException {
        // Arrange
        when(mockResultSet.getInt("id")).thenReturn(0);
        when(mockResultSet.getString("title")).thenReturn(null);
        when(mockResultSet.getString("artist")).thenReturn(null);
        when(mockResultSet.getInt("duration")).thenReturn(0);
        when(mockResultSet.getString("album")).thenReturn(null);

        // Act
        TrackDTO result = trackMapper.mapToDTO(mockResultSet);

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getId());
        assertNull(result.getTitle());
        assertNull(result.getPerformer());
        assertEquals(0, result.getDuration());
        assertNull(result.getAlbum());

        // Verify that ResultSet methods were still accessed
        verify(mockResultSet, times(1)).getInt("id");
        verify(mockResultSet, times(1)).getString("title");
        verify(mockResultSet, times(1)).getString("artist");
        verify(mockResultSet, times(1)).getInt("duration");
        verify(mockResultSet, times(1)).getString("album");
    }

}
