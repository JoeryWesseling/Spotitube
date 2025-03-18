package nl.han.oose.dea.data.mappers;

import nl.han.oose.dea.dto.LoginResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoginMapperTest {

    private LoginMapper loginMapper;
    private ResultSet mockResultSet;

    @BeforeEach
    void setup() {
        loginMapper = new LoginMapper();
        mockResultSet = mock(ResultSet.class);
    }

    @Test
    void testMapToDTO_WithValidResultSet() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("username")).thenReturn("Frodo");
        when(mockResultSet.getString("token")).thenReturn("some-token");

        // Act
        LoginResponseDTO result = loginMapper.mapToDTO(mockResultSet);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Frodo", result.getUser());
        assertEquals("some-token", result.getToken());

        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, times(1)).getInt("id");
        verify(mockResultSet, times(1)).getString("username");
        verify(mockResultSet, times(1)).getString("token");
    }

    @Test
    void testMapToDTO_WithEmptyResultSet() throws SQLException {
        // Arrange
        when(mockResultSet.next()).thenReturn(false);

        // Act
        LoginResponseDTO result = loginMapper.mapToDTO(mockResultSet);

        // Assert
        assertNull(result);

        verify(mockResultSet, times(1)).next();
        verify(mockResultSet, never()).getInt(anyString());
        verify(mockResultSet, never()).getString(anyString());
    }
}
