package nl.han.oose.dea.service;

import nl.han.oose.dea.dto.TrackDTO;

import java.util.List;

public interface ITrackService {

    public List<TrackDTO> getAllByPlaylists(int playlistId);

    public boolean addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable);

    public List<TrackDTO> getAvailableTracks(Integer playlistId);

    public List<TrackDTO> getAllTracks();

    public void removeTrackFromPlaylist(int playlistId, int trackId);

}
