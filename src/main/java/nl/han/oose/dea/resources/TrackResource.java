package nl.han.oose.dea.resources;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.dto.TrackResponseDTO;
import nl.han.oose.dea.service.ITokenService;
import nl.han.oose.dea.service.ITrackService;
import nl.han.oose.dea.service.TokenService;
import nl.han.oose.dea.service.TrackService;

import java.util.List;

@Path("/tracks")
@ApplicationScoped
public class TrackResource extends BaseResource {
    @Inject
    private ITokenService tokenService;

    @Inject
    private ITrackService trackService;

    private static final String INVALID_TOKEN = "{\"error\": \"Invalid or missing token\"}";


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAvailableTracks(@QueryParam("forPlaylist") Integer playlistId, @QueryParam("token") String token) {
        String currentUser = authenticate(token);

        List<TrackDTO> tracks;
        if (playlistId != null) {
            tracks = trackService.getAvailableTracks(playlistId);
        } else {
            tracks = trackService.getAllTracks();
        }
        TrackResponseDTO responseDTO = buildTrackResponse(tracks);
        return Response.ok(responseDTO).build();
    }

    private TrackResponseDTO buildTrackResponse(List<TrackDTO> tracks) {
        int totalDuration = tracks.stream().mapToInt(TrackDTO::getDuration).sum();

        return new TrackResponseDTO(tracks);
    }
}
