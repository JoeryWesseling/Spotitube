package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.data.queries.PlaylistQueries;
import nl.han.oose.dea.dto.PlayListDTO;
import nl.han.oose.dea.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface PlaylistDAO {
     List<PlayListDTO> getAllPlaylists();
     boolean isOwner(int playlistId, String username);
     boolean updatePlaylistName(int playlistId, String newName);
     int getNextPlaylistId();
     void addPlaylist(PlayListDTO newList, String username);
     boolean deletePlaylist(int playlistId) ;
}
