package nl.han.oose.dea.DAO;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class TrackDAO {

    private final List<TrackDTO> allTracks = new ArrayList<>();

    public TrackDAO() {
        allTracks.add(new TrackDTO(1, "The Pelenor Fields", "Howard Shore", 355, "Return of the King"));
        allTracks.add(new TrackDTO(2, "A Storm is Coming", "Howard Shore", 300, "Return of the King"));
        allTracks.add(new TrackDTO(5, "The Blacker the Berry", "Kendrick Lamar", 360, "To Pimp A Butterfly"));
        allTracks.add(new TrackDTO(6, "Mother I", "Kendrick Lamar", 360, "Mr. Morale & The Big Steppers"));
    }

    public List<TrackDTO> getAllTracks() {
        return allTracks;
    }
}
