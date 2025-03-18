package nl.han.oose.dea.dto;

import java.util.List;

public class PlayListDTO {

    private int id;
    private String name;
    private boolean isOwner;
    private List<TrackDTO> tracks;

    public PlayListDTO(){
    }

    public PlayListDTO(int id, String name, boolean owner,List<TrackDTO> tracks){
        this.id = id;
        this.name = name;
        this.isOwner = owner;
        this.tracks = tracks;
    }





    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isOwner() {
        return isOwner;
    }

    public void setOwner(boolean owner) {
        this.isOwner = owner;
    }

    public List<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(List<TrackDTO> tracks) {
        this.tracks = tracks;
    }


}
