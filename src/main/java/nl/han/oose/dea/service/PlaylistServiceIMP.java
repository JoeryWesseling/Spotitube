package nl.han.oose.dea.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import nl.han.oose.dea.data.dao.PlaylistDAO;
import nl.han.oose.dea.dto.PlayListDTO;

import java.util.List;

@ApplicationScoped
public class PlaylistServiceIMP implements PlaylistService{

    @Inject
    private PlaylistDAO playlistDAO;
    @Inject
    private TrackServiceIMP trackServiceIMP;
    @Inject
    private TokenServiceIMP tokenServiceIMP;

    public List<PlayListDTO> getAllPlayLists() {
        List<PlayListDTO> playlists = playlistDAO.getAllPlaylists();

        for (PlayListDTO playListDTO : playlists) {
            playListDTO.setTracks(trackServiceIMP.getAllByPlaylists(playListDTO.getId()));
        }

        return playlists;
    }

    public boolean updatePlaylistName(int playlistId, String name, String token) {
        String username = tokenServiceIMP.getUsernameFromToken(token);

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
