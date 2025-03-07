package nl.han.oose.dea.data.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.PlayListDTO;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
@ApplicationScoped
public class PlaylistMapper implements IMapper<PlayListDTO> {
    @Override
    public PlayListDTO mapToDTO(ResultSet rs) throws SQLException {

        return new PlayListDTO(
                rs.getInt("id"),
                rs.getString("name"),
                rs.getBoolean("isOwner"),
                List.of()
        );
    }
}
