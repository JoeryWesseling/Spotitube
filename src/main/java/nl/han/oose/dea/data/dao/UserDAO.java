package nl.han.oose.dea.data.dao;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.dto.LoginResponseDTO;
import nl.han.oose.dea.data.database.DatabaseConnection;
import nl.han.oose.dea.data.mappers.LoginMapper;
import nl.han.oose.dea.data.queries.UserQueries;
import nl.han.oose.dea.exceptions.DatabaseException;
import nl.han.oose.dea.exceptions.UnauthorizedException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


@ApplicationScoped
public class UserDAO implements IUserDAO{
    @Inject
    private DatabaseConnection databaseConnection;

    @Inject
    private LoginMapper loginMapper;


    public LoginResponseDTO getUserByUsername(String username, String password) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserQueries.LOGIN_QUERY)) {

            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return new LoginResponseDTO(
                        rs.getInt("id"),
                        null,
                        rs.getString("username")
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fout bij ophalen gegevens gebruiker", e);
        }
        return null;
    }

    public boolean addToken(LoginResponseDTO user) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserQueries.ADD_TOKEN_QUERY)) {

            ps.setString(1, user.getToken());
            ps.setInt(2, user.getId());
            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            throw new DatabaseException("Fout bij toevoegen token", e);
        }
    }

    public LoginResponseDTO verifyToken(String token) throws UnauthorizedException {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserQueries.FETCH_USER_BY_TOKEN_QUERY)) {

            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new LoginResponseDTO(
                        rs.getInt("id"),
                        token,
                        rs.getString("username")
                );
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fout bij verify token", e);
        }
        throw new UnauthorizedException();


    }


    public String getUserByToken(String token) {
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(UserQueries.FETCH_USER_BY_TOKEN_QUERY)) {
            ps.setString(1, token);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("username");
            }
        } catch (SQLException e) {
            throw new DatabaseException("No username found in database that matches this token", e);
        }
        return null;
    }
}
