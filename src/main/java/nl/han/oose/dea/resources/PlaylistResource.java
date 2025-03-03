package nl.han.oose.dea.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.PlayListResponseDTO;
import nl.han.oose.dea.DTO.TrackDTO;
import nl.han.oose.dea.service.PlaylistService;
import nl.han.oose.dea.service.TokenService;


@Path("/playlists")
public class PlaylistResource {

    @Inject
    private TokenService tokenService;

    @Inject
    private PlaylistService playlistService;


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayLists(@QueryParam("token") String token){

        if(token == null || !tokenService.isValidToken(token)){
            return Response.status(Response.Status.UNAUTHORIZED).entity("{\"error\": \"Missing token\"}").build();
        }

        var playlists = playlistService.getAllPlayLists();
        int totalLength = playlists.stream().flatMap(p -> p.getTracks().stream()).mapToInt(TrackDTO::getDuration).sum();

        return Response.ok(new PlayListResponseDTO(playlists,totalLength)).build();
    }
}
