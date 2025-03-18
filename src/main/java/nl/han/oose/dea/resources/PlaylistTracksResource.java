package nl.han.oose.dea.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.data.domain.Track;
import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.dto.TrackResponseDTO;
import nl.han.oose.dea.data.dao.PlaylistDAO;
import nl.han.oose.dea.service.TokenService;
import nl.han.oose.dea.service.TrackService;

import java.util.List;

@Path("/playlists/{playlist_id}/tracks")
public class PlaylistTracksResource extends BaseResource {

    @Inject
    private TrackService trackService;

    @Inject
    private PlaylistDAO playlistDAO;

    private static final String INVALID_TOKEN = "{\"error\": \"Invalid or missing token\"}";


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tracks(@PathParam("playlist_id") int playlistId, @QueryParam("token") String token) {
        String currentUser = authenticate(token);
        List<TrackDTO> tracks = trackService.getAllByPlaylists(playlistId);
        return Response.ok(new TrackResponseDTO(tracks)).build();

    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("playlist_id") int playlistId, @QueryParam("token") String token, TrackDTO trackRequest) {
        String username = authenticate(token);

        if (!playlistDAO.isOwner(playlistId, username)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(INVALID_TOKEN)
                    .build();
        }
        boolean added = trackService.addTrackToPlaylist(playlistId, trackRequest.getId(), trackRequest.isOfflineAvailable());
        if (!added) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(INVALID_TOKEN)
                    .build();
        }
        List<TrackDTO> updatedTracks = trackService.getAllByPlaylists(playlistId);
        return Response.ok(new TrackResponseDTO(updatedTracks)).build();
    }


    @DELETE
    @Path("/{track_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@PathParam("playlist_id") int playlistId, @QueryParam("token") String token, @PathParam("track_id") int trackId) {
        String username = authenticate(token);

        if (!playlistDAO.isOwner(playlistId, username)) {
            return Response.status(Response.Status.FORBIDDEN)
                    .entity(INVALID_TOKEN)
                    .build();
        }
        trackService.removeTrackFromPlaylist(playlistId, trackId);
        List<TrackDTO> updatedTracks = trackService.getAllByPlaylists(playlistId);
        return Response.ok(new TrackResponseDTO(updatedTracks)).build();

    }
}
