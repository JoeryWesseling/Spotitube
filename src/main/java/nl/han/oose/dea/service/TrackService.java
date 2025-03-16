package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.data.DAO.TrackDAO;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    public List<TrackDTO> getAllTracks(int playlistId) {
        return trackDAO.getAllTracks(playlistId);
    }


}




