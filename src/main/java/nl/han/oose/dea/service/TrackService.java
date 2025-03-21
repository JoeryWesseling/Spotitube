package nl.han.oose.dea.service;

import nl.han.oose.dea.dto.TrackDTO;

import java.util.List;

public interface TrackService {

    List<TrackDTO> getAllByPlaylists(int playlistId);

    boolean addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable);

    List<TrackDTO> getAvailableTracks(Integer playlistId);

    List<TrackDTO> getAllTracks();

    void removeTrackFromPlaylist(int playlistId, int trackId);

}
