package nl.han.oose.dea.data.queries;

public class PlaylistQueries {
        public static final String IS_OWNER = "SELECT COUNT(*) FROM playlists WHERE id = ? AND owner = ?";
        public static final String UPDATE_NAME = "UPDATE playlists SET name = ? WHERE id = ?";
        public static final String GET_NEXT_ID = "SELECT MAX(id) FROM playlists";
        public static final String ADD_PLAYLIST = "INSERT INTO playlists (id, name, owner, isOwner) VALUES (?, ?, ?, ?)";
        public static final String GET_ALL_PLAYLISTS = "SELECT * FROM playlists";
        public static final String DELETE_PLAYLIST = "DELETE FROM playlists WHERE id = ?";
}
