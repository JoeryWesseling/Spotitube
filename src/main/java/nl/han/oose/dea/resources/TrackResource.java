package nl.han.oose.dea.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.TrackResponseDTO;
import nl.han.oose.dea.service.TokenService;
import nl.han.oose.dea.service.TrackService;

@Path("/playlists/{playlist_id}/tracks")
public class TrackResource {

    @Inject
    private TokenService tokenService;

    @Inject
    private TrackService trackService;

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
}
