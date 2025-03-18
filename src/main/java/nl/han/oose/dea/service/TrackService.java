package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.data.dao.TrackDAO;
import nl.han.oose.dea.dto.TrackDTO;

import java.util.List;


@ApplicationScoped
public class TrackService {

    @Inject
    private TrackDAO trackDAO;

    public List<TrackDTO> getAllByPlaylists(int playlistId) {

        return trackDAO.getAllTracks(playlistId);
    }

    public boolean addTrackToPlaylist(int playlistId, int trackId, boolean offlineAvailable) {
        return trackDAO.addTrackToPlaylist(playlistId, trackId, offlineAvailable);
    }

    public List<TrackDTO> getAvailableTracks(Integer playlistId) {
        return trackDAO.getAvailableTracks(playlistId);

    }

    public List<TrackDTO> getAllTracks() {
        return trackDAO.getAllTracksNoId();
    }


    public void removeTrackFromPlaylist(int playlistId, int trackId) {
        trackDAO.removeTrackFromPlaylist(playlistId,trackId);
    }
}




