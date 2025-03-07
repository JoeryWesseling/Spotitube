package nl.han.oose.dea.data.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.TrackDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class TrackMapper implements IMapper<TrackDTO> {
    @Override
    public TrackDTO mapToDTO(ResultSet rs) throws SQLException {

        return new TrackDTO(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("artist"),
                rs.getInt("duration"),
                rs.getString("album")
        );
    }
}
