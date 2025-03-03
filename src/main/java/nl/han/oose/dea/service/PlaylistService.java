package nl.han.oose.dea.service;


import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ApplicationScoped
public class PlaylistService {
    private List<PlayListDTO> playlists = new ArrayList<>();


    List<TrackDTO> lotrMusic = Arrays.asList(
            new TrackDTO(1,"The Fields Of Pelenor","Howard Shore",212,"Return of the King"),
            new TrackDTO(2,"A Storm is coming", "Howard Shore",212,"Return of the King"));

    public PlaylistService(){
        playlists.add(new PlayListDTO(1, "Lord of the rings music", true,lotrMusic));
        playlists.add(new PlayListDTO(2, "Gym Playlist", false,lotrMusic));
        playlists.add(new PlayListDTO(3, "I suck at coding 101 playlist", true,lotrMusic));
    }

    public List<PlayListDTO> getAllPlayLists(){
        return playlists;
    }
}
