package nl.han.oose.dea.data.mappers;

import nl.han.oose.dea.DTO.PlayListDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PlaylistMapperTest {

    private PlaylistMapper playlistMapper;
    private ResultSet mockResultSet;

    @BeforeEach
    void setup() {
        playlistMapper = new PlaylistMapper();
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void testMapToDTO_WithValidResultSet() throws SQLException {
        // Arrange
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("Chill Vibes");
        when(mockResultSet.getBoolean("isOwner")).thenReturn(true);

        // Act
        PlayListDTO result = playlistMapper.mapToDTO(mockResultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Chill Vibes", result.getName());
        assertTrue(result.isOwner());
        assertNotNull(result.getTracks());
        assertEquals(0, result.getTracks().size());

        verify(mockResultSet, times(1)).getInt("id");
        verify(mockResultSet, times(1)).getString("name");
        verify(mockResultSet, times(1)).getBoolean("isOwner");
    }

    @Test
    void testMapToDTO_WithEmptyResultSet() throws SQLException {
        // Arrange
        when(mockResultSet.getInt("id")).thenThrow(new SQLException("No data"));

        // Act & Assert
        assertThrows(SQLException.class, () -> playlistMapper.mapToDTO(mockResultSet));

        verify(mockResultSet, never()).getString(anyString());
        verify(mockResultSet, never()).getBoolean(anyString());
    }
}
