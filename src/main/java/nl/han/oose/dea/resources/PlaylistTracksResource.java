package nl.han.oose.dea.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.DTO.TrackResponseDTO;
import nl.han.oose.dea.data.DAO.PlaylistDAO;
import nl.han.oose.dea.service.TokenService;
import nl.han.oose.dea.service.TrackService;

import java.util.List;

@Path("/playlists/{playlist_id}/tracks")
public class PlaylistTracksResource {

    @Inject
    private TokenService tokenService;

    @Inject
    private TrackService trackService;
    @Inject
    private PlaylistDAO playlistDAO;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response tracks(@PathParam("playlist_id") int playlistId, @QueryParam("token") String token) {

        if(token == null || !tokenService.isValidToken(token)){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"invalid or missing token\"}")
                    .build();
        }

        TrackResponseDTO trackResponseDTO = new TrackResponseDTO();
        trackResponseDTO.setTracks(trackService.getAllByPlaylists(playlistId));
        return Response.ok(trackResponseDTO).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addTrackToPlaylist(@PathParam("playlist_id") int playlistId, @QueryParam("token") String token, TrackDTO trackRequest){

        if(token == null || !tokenService.isValidToken(token)){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }
        String username = tokenService.getUsernameFromToken(token);

        if(!playlistDAO.isOwner(playlistId,username)){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"You are not the owner of this playlist\"}")
                    .build();
        }

        boolean added = trackService.addTrackToPlaylist(playlistId, trackRequest.getId(), trackRequest.isOfflineAvailable());
        if(!added){
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"Failed to add track to playlist\"}")
                    .build();
        }

        List<TrackDTO> updatedTracks = trackService.getAllByPlaylists(playlistId);
        return Response.ok(new TrackResponseDTO(updatedTracks)).build();
    }
    @DELETE
    @Path("/{track_id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeTrackFromPlaylist(@PathParam("playlist_id") int playlistId,@QueryParam("token") String token,@PathParam("track_id") int trackId){
        if(token == null || !tokenService.isValidToken(token)){
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }
        String username = tokenService.getUsernameFromToken(token);

        if(!playlistDAO.isOwner(playlistId,username)){
            return Response.status(Response.Status.FORBIDDEN)
                    .entity("{\"error\": \"You are not the owner of this playlist\"}")
                    .build();
        }

        trackService.removeTrackFromPlaylist(playlistId,trackId);

        var updatedTracks = trackService.getAllByPlaylists(playlistId);
        return Response.ok(new TrackResponseDTO(updatedTracks)).build();



    }
}
