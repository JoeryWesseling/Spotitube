package nl.han.oose.dea.DTO;

import java.util.List;

public class TrackResponseDTO {


    public List<TrackDTO> tracks;

    public TrackResponseDTO(){
    }

    public TrackResponseDTO(List<TrackDTO> tracks){
        this.tracks = tracks;
    }

    public void setTracks(List<TrackDTO>tracks){
        this.tracks = tracks;
    }
    public List<TrackDTO> getTracks(){
        return tracks;
    }
}
