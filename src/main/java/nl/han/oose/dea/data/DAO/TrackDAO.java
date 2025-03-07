package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.TrackMapper;
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
        String sql = "SELECT t.* FROM tracks t JOIN playlist_tracks pt ON t.id = pt.track_id WHERE pt.playlist_id = ?";

        try (Connection conn = databaseConnection.getConnection();

             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, playlistId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    tracks.add(trackMapper.mapToDTO(rs));
                }
            }
        }catch (SQLException e) {
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
            throw new DatabaseException("Fout bij het ophalen van de tracks",e);
        }

        return tracks;
    }
}
