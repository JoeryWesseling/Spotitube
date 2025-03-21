package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.data.queries.TrackQueries;
import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.exceptions.DatabaseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ITrackDAO {

    public List<TrackDTO> getAllTracks(int playlistId);

    public List<TrackDTO> getAllTracksNoId();

    public boolean addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable);

    public TrackDTO getTrackById(int trackId);

    public List<TrackDTO> getAvailableTracks(Integer playlistId);

    public void removeTrackFromPlaylist(int playlistId, int trackId);
}
