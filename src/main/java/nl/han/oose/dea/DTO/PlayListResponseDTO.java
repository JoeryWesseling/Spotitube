package nl.han.oose.dea.DTO;

import java.util.List;

public class PlayListResponseDTO {

    private List<PlayListDTO> playlists;
    private int length;

    public PlayListResponseDTO(List<PlayListDTO>playlists,int length){
        this.playlists = playlists;
        this.length = length;
    }


    public List<PlayListDTO> getPlaylists() {
        return playlists;
    }

    public void setPlaylists(List<PlayListDTO> playlists) {
        this.playlists = playlists;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }
}
