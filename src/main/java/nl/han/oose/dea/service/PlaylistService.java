package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.DTO.PlayListDTO;
import nl.han.oose.dea.data.DAO.PlaylistDAO;

import java.util.List;

@ApplicationScoped
public class PlaylistService {

    @Inject
    private PlaylistDAO playlistDAO;

    @Inject
    private TrackService trackService;
    @Inject
    private TokenService tokenService;

    public List<PlayListDTO> getAllPlayLists() {
        List<PlayListDTO> playlists = playlistDAO.getAllPlaylists();

        for (PlayListDTO playListDTO : playlists) {
            playListDTO.setTracks(trackService.getAllByPlaylists(playListDTO.getId()));
        }

        return playlists;
    }

    public boolean updatePlaylistName(int playlistId, String name, String token) {
        String username = tokenService.getUsernameFromToken(token);

        if(!playlistDAO.isOwner(playlistId,username)){
            return false;
        }
        return playlistDAO.updatePlaylistName(playlistId,name);
    }
}
