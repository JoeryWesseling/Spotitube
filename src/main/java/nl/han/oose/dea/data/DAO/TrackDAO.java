package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.TrackMapper;
import nl.han.oose.dea.data.queries.TrackQueries;
import nl.han.oose.dea.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class TrackDAO {

    @Inject
    private TrackMapper trackMapper;

    @Inject
    private DatabaseConnection databaseConnection;

//    private final List<TrackDTO> allTracks = new ArrayList<>();
//
//    public TrackDAO() {
//        allTracks.add(new TrackDTO(1, "The Pelenor Fields", "Howard Shore", 355, "Return of the King"));
//        allTracks.add(new TrackDTO(2, "A Storm is Coming", "Howard Shore", 300, "Return of the King"));
//        allTracks.add(new TrackDTO(5, "The Blacker the Berry", "Kendrick Lamar", 360, "To Pimp A Butterfly"));
//        allTracks.add(new TrackDTO(6, "Mother I", "Kendrick Lamar", 360, "Mr. Morale & The Big Steppers"));
//    }


    public List<TrackDTO> getAllTracks(int playlistId) {
        List<TrackDTO> tracks = new ArrayList<>();

        try (Connection conn = databaseConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(TrackQueries.GET_ALL_TRACKS)) {

            ps.setInt(1, playlistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tracks.add(trackMapper.mapToDTO(rs));
                }
            }
        } catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseException("Fout bij het ophalen van de tracks", e);
        }

        return tracks;
    }

    public boolean addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        try (Connection conn = databaseConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(TrackQueries.ADD_TRACK_TO_PLAYLIST)) {

            ps.setInt(1, playlistId);
            ps.setInt(2, trackId);
            ps.setBoolean(3,offlineAvailable);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new DatabaseException("error adding track to playlist", e);
        }
    }

    public TrackDTO getTrackById(int trackId) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(TrackQueries.GET_TRACK_BY_ID)) {

            ps.setInt(1, trackId);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return trackMapper.mapToDTO(rs);
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving track", e);
        }
        return null;
    }

    public List<TrackDTO> getAvailableTracks(Integer playlistId) {
        List<TrackDTO> availableTracks = new ArrayList<>();
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(TrackQueries.GET_ALL_TRACKS_NOT_INPLAYLIST)) {

            ps.setInt(1, playlistId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    availableTracks.add(trackMapper.mapToDTO(rs));
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error retrieving available tracks", e);
        }

        return availableTracks;
    }

    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        try(Connection conn = databaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement(TrackQueries.REMOVE_TRACK_FROM_PLAYLIST)){

            ps.setInt(1,playlistId);
            ps.setInt(2,trackId);
            ps.executeUpdate();

        } catch (SQLException e){
            throw new DatabaseException("Error removing track from playlist.",e);
        }
    }
}


