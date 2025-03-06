package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.data.mappers.PlaylistMapper;

import java.sql.Connection;
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

//    private final List<PlayListDTO> playlists = new ArrayList<>();
//
//    public PlaylistDAO() {
//
//    }

    public List<PlayListDTO> getAllPlaylists() {
        List<PlayListDTO> playlists = new ArrayList<>();
        try(Connection conn = new dataBaseConnection.getConnection();
            PreparedStatements ps = conn.prepareStatement("SELECT * FROM playlists");
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                playlists.add(playlistMapper.mapToDTO(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return playlists;
    }
}
