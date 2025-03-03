package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class TrackService {


    public List<TrackDTO> getTracksForPlayList(int playlistId){

        List<TrackDTO> tracks = new ArrayList<>();

        tracks.add(new TrackDTO(1,"The Pelenor Fields","Howard Shore",355,"Return of the King"));
        tracks.add(new TrackDTO(2,"A Storm is coming","Howard Shore",300,"Return of the King"));


        return tracks;
    }


}
