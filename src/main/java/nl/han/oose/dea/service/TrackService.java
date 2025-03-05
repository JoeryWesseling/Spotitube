package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DAO.TrackDAO;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class TrackService {

    @Inject
    private TrackDAO trackDAO;

    public List<TrackDTO> getAllByPlaylists(int playlistId){
        return trackDAO.getTracksForPlaylist(playlistId);
    }



}
