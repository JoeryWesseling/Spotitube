package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.PlaylistMapper;
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
        try(Connection conn = databaseConnection.getConnection();
            PreparedStatement ps = conn.prepareStatement("select * from playlists");
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                playlists.add(playlistMapper.mapToDTO(rs));
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fout bij het ophalen playlistys",e);
        }
        return playlists;
    }
}
