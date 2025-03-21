package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.data.dao.IPlaylistDAO;
import nl.han.oose.dea.dto.PlayListDTO;
import nl.han.oose.dea.data.dao.PlaylistDAO;

import java.util.List;

@ApplicationScoped
public class PlaylistService implements IPlaylistService{

    @Inject
    private IPlaylistDAO playlistDAO;
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

    public void addPlaylist(PlayListDTO newList, String username) {

        int newId = playlistDAO.getNextPlaylistId();

        newList.setId(newId);
        newList.setOwner(true);

        playlistDAO.addPlaylist(newList,username);
    }

    public boolean deletePlaylist(int playlistId, String username) {
        if(!playlistDAO.isOwner(playlistId,username)){
           return false;
        }
        return playlistDAO.deletePlaylist(playlistId);
    }
}
