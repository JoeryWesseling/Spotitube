package nl.han.oose.dea.data.queries;

public class TrackQueries {
    public static final String ADD_TRACK_TO_PLAYLIST = "INSERT INTO playlist_tracks (playlist_id, track_id, offline_available) VALUES (?, ?, ?)";
    public static final String GET_TRACK_BY_ID = "SELECT * FROM tracks WHERE id = ?";
    public static final String REMOVE_TRACK_FROM_PLAYLIST = "DELETE FROM playlist_tracks WHERE playlist_id = ? AND track_id = ?";
    public static final String GET_ALL_TRACKS = "SELECT t.* FROM tracks t JOIN playlist_tracks pt ON t.id = pt.track_id WHERE pt.playlist_id = ?";
    public static final String GET_ALL_TRACKS_NO_ID = "SELECT t.* FROM tracks t JOIN playlist_tracks pt ON t.id = pt.track_id";
    public static final String GET_ALL_TRACKS_NOT_INPLAYLIST = "SELECT * FROM tracks WHERE id NOT IN (SELECT track_id FROM playlist_tracks WHERE playlist_id = ?)";


}
