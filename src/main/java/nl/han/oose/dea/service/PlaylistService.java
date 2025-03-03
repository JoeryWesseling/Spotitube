package nl.han.oose.dea.service;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PlaylistService {

    @Inject
    private TrackService trackService;

    private List<PlayListDTO> playlists = new ArrayList<>();


    public PlaylistService(){
        playlists.add(new PlayListDTO(1, "Lord of the rings music", true,null));
        playlists.add(new PlayListDTO(2, "Gym Playlist", false,null));
        playlists.add(new PlayListDTO(3, "I suck at coding 101 playlist", true,null));
        playlists.add(new PlayListDTO(4,"EggyList",true,null));
    }

    public List<PlayListDTO> getAllPlayLists(){
        for(PlayListDTO playlist :playlists){
            playlist.setTracks(trackService.getAllByPlaylist(playlist.getId()));
        }
        return playlists;
    }
}
