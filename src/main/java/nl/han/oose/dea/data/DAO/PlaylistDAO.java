package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.PlaylistMapper;
import nl.han.oose.dea.data.queries.PlaylistQueries;
import nl.han.oose.dea.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaylistDAO {

    @Inject
    private PlaylistMapper playlistMapper;

    @Inject
    private DatabaseConnection databaseConnection;




    public List<PlayListDTO> getAllPlaylists() {
        List<PlayListDTO> playlists = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PlaylistQueries.GET_ALL_PLAYLISTS);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                playlists.add(playlistMapper.mapToDTO(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fout bij het ophalen playlists", e);
        }
        return playlists;
    }

    public boolean isOwner(int playlistId, String username) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PlaylistQueries.IS_OWNER)) {

            ps.setInt(1, playlistId);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                return true;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error checking for owner", e);
        }
        return false;
    }

    public boolean updatePlaylistName(int playlistId, String newName) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PlaylistQueries.UPDATE_NAME)) {

            ps.setString(1, newName);
            ps.setInt(2, playlistId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error updating name", e);
        }
    }

    public int getNextPlaylistId() {

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PlaylistQueries.GET_NEXT_ID);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getInt(1) + 1;
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error getting next playlistID", e);

        }
        return 1;
    }

    public void addPlaylist(PlayListDTO newList, String username) {

        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PlaylistQueries.ADD_PLAYLIST)) {

            ps.setInt(1, newList.getId());
            ps.setString(2, newList.getName());
            ps.setString(3, username);
            ps.setBoolean(4, true);

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Error adding playlist", e);
        }
    }


    public boolean deletePlaylist(int playlistId) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(PlaylistQueries.DELETE_PLAYLIST)) {

            ps.setInt(1, playlistId);

            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("Error adding playlist", e);
        }

    }
}
