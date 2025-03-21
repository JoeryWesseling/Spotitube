package nl.han.oose.dea.service;

import nl.han.oose.dea.dto.PlayListDTO;

import java.util.List;

public interface IPlaylistService {
    public List<PlayListDTO> getAllPlayLists();

    public boolean updatePlaylistName(int playlistId, String name, String token);

    public void addPlaylist(PlayListDTO newList, String username);

    public boolean deletePlaylist(int playlistId, String username);

}
