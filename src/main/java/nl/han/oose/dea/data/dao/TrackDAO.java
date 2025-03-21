package nl.han.oose.dea.data.dao;

import nl.han.oose.dea.dto.TrackDTO;

import java.util.List;


 public interface TrackDAO {

     List<TrackDTO> getAllTracks(int playlistId);

     List<TrackDTO> getAllTracksNoId();

     boolean addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable);

     TrackDTO getTrackById(int trackId);

     List<TrackDTO> getAvailableTracks(Integer playlistId);

     void removeTrackFromPlaylist(int playlistId, int trackId);
}
