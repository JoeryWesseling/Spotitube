package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import nl.han.oose.dea.DTO.TrackDTO;

import java.util.ArrayList;
import java.util.List;


@ApplicationScoped
public class TrackService {


    public List<TrackDTO> getAllNotInPlayList(int playlistId) {
        List<TrackDTO> allTracks = getMockTracks();
        List<TrackDTO> filteredTracks = new ArrayList<>();

        for (TrackDTO track : allTracks) {
            if (track.getId() % playlistId != 0) {
                filteredTracks.add(track);
            }
        }
        return filteredTracks;
    }

    public List<TrackDTO> getAllByPlaylist(int playlistId){
        List<TrackDTO> playlistTracks = new ArrayList<>();

        switch (playlistId) {
            case 1:
                playlistTracks.add(new TrackDTO(1, "The Fields Of Pelennor", "Howard Shore", 355, "Return of the King"));
                playlistTracks.add(new TrackDTO(2, "A Storm is Coming", "Howard Shore", 300, "Return of the King"));
                break;
            case 2:
                playlistTracks.add(new TrackDTO(3, "Eye of the Tiger", "Survivor", 240, "Rocky III"));
                playlistTracks.add(new TrackDTO(4, "Lose Yourself", "Eminem", 326, "8 Mile"));
                break;
            case 3:
                playlistTracks.add(new TrackDTO(5, "Lo-Fi Coding Beats", "ChilledCow", 180, "Lo-Fi Essentials"));
                playlistTracks.add(new TrackDTO(6, "Relaxing Piano", "Yiruma", 240, "River Flows in You"));
                break;
            case 4:
                playlistTracks.add(new TrackDTO(7, "The Blacker the Berry", "Kendrick Lamar", 360, "To Pimp A Butterfly"));
                playlistTracks.add(new TrackDTO(8, "Mother I", "Kendrick Lamar", 360, "Mr. Morale & The Big Steppers"));
                playlistTracks.add(new TrackDTO(9, "Auntie Diaries", "Kendrick Lamar", 360, "Mr. Morale & The Big Steppers"));
                playlistTracks.add(new TrackDTO(10, "Wesley's Theory", "Kendrick Lamar", 360, "To Pimp A Butterfly"));
                break;
            default:
                break;
        }
        return playlistTracks;
    }



    private List<TrackDTO> getMockTracks() {
        List<TrackDTO> tracks = new ArrayList<>();
        tracks.add(new TrackDTO(1, "The Pelenor Fields", "Howard Shore", 355, "Return of the King"));
        tracks.add(new TrackDTO(2, "A Storm is coming", "Howard Shore", 300, "Return of the King"));
        tracks.add(new TrackDTO(3, "Eye of the Tiger", "Survivor", 245, "Rocky III"));
        tracks.add(new TrackDTO(4, "Lose Yourself", "Eminem", 326, "8 Mile"));
        tracks.add(new TrackDTO(5,"The Blacker the berry", "Kendrick Llamar",360,"To Pimp A Butterfly"));
        tracks.add(new TrackDTO(6,"Mother i ", "Kendrick Llamar",360,"Mr Morale And the Big Steppers"));
        tracks.add(new TrackDTO(7,"Auntie Diaries", "Kendrick Llamar",360,"Mr Morale And the Big Steppers"));
        tracks.add(new TrackDTO(8,"Wesleys Theory", "Kendrick Llamar",360,"To Pimp A Butterfly"));




        return tracks;
    }


}
