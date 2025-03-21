package nl.han.oose.dea.data.mappers;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.dto.LoginResponseDTO;

import java.sql.ResultSet;
import java.sql.SQLException;

@ApplicationScoped
public class LoginMapper implements IMapper{
    @Override
    public LoginResponseDTO mapToDTO(ResultSet rs) throws SQLException {
        if(rs.next()){
            return new LoginResponseDTO(
                    rs.getInt("id"),
                    rs.getString("token"),
                    rs.getString("username")
            );
        }
        return null;
    }
}



