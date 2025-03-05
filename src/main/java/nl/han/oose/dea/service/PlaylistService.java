package nl.han.oose.dea.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DAO.PlaylistDAO;
import nl.han.oose.dea.DAO.TrackDAO;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PlaylistService {

    @Inject
    private PlaylistDAO playlistDAO;

    @Inject
    private TrackDAO trackDAO;

    public List<PlayListDTO> getAllPlayLists() {
        List<PlayListDTO> playlists = playlistDAO.getAllPlaylistst();

        for (PlayListDTO playListDTO : playlists) {
            playListDTO.setTracks(trackDAO.getTracksForPlaylist(playListDTO.getId()));
        }
        return playlists;
    }
}
