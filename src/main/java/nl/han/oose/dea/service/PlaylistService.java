package nl.han.oose.dea.service;

import nl.han.oose.dea.dto.PlayListDTO;

import java.util.List;

public interface PlaylistService {
     List<PlayListDTO> getAllPlayLists();

     boolean updatePlaylistName(int playlistId, String name, String token);

     void addPlaylist(PlayListDTO newList, String username);

     boolean deletePlaylist(int playlistId, String username);

}
