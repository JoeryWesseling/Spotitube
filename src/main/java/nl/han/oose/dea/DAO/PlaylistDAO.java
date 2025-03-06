package nl.han.oose.dea.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class PlaylistDAO {

    private final List<PlayListDTO> playlists = new ArrayList<>();

    public PlaylistDAO() {
        List<TrackDTO> lotrMusic = List.of(
                new TrackDTO(1, "The Fields Of Pelenor", "Howard Shore", 212, "Return of the King"),
                new TrackDTO(2, "A Storm is Coming", "Howard Shore", 212, "Return of the King")
        );

        playlists.add(new PlayListDTO(1, "Lord of the Rings Music", true, lotrMusic));
        playlists.add(new PlayListDTO(2, "Gym Playlist", false, new ArrayList<>()));
        playlists.add(new PlayListDTO(3, "Coding is hard, and I suck", true, new ArrayList<>()));
    }

    public List<PlayListDTO> getAllPlaylists() {
        return playlists;
    }
}
