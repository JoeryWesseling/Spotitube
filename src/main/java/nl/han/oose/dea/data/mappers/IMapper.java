package nl.han.oose.dea.data.mappers;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface IMapper<T> {
        T mapToDTO(ResultSet rs) throws SQLException;
}
