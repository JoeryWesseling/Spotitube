package nl.han.oose.dea.resources;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.DTO.TrackResponseDTO;
import nl.han.oose.dea.service.TokenService;
import nl.han.oose.dea.service.TrackService;

import java.util.List;

@Path("/tracks")
@ApplicationScoped
public class TrackResource {
    @Inject
    private TokenService tokenService;

    @Inject
    private TrackService trackService;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableTracks(@QueryParam("forPlaylist") Integer playlistId, @QueryParam("token") String token) {
        if (token == null || !tokenService.isValidToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }

        List<TrackDTO> tracks;
        if(playlistId != null){
            tracks = trackService.getAvailableTracks(playlistId);

        } else {
            tracks = trackService.getAllTracks(playlistId);
        }
        return Response.ok(new TrackResponseDTO(tracks)).build();
    }
}
