package nl.han.oose.dea.resources;


import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.data.dao.IPlaylistDAO;
import nl.han.oose.dea.data.dao.PlaylistDAO;
import nl.han.oose.dea.dto.PlayListDTO;
import nl.han.oose.dea.dto.PlayListResponseDTO;
import nl.han.oose.dea.dto.TrackDTO;
import nl.han.oose.dea.service.IPlaylistService;
import nl.han.oose.dea.service.PlaylistService;
import nl.han.oose.dea.service.TokenService;


@Path("/playlists")
@ApplicationScoped
public class PlaylistResource extends BaseResource {


    @Inject
    private IPlaylistService playlistService;

    @Inject
    private IPlaylistDAO playlistDAO;

    private static final String INVALID_TOKEN = "{\"error\": \"Invalid or missing token\"}";


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getPlayLists(@QueryParam("token") String token) {

        String currentUser = authenticate(token);
        PlayListResponseDTO responseDTO = buildPlayListResponse(currentUser);
        return Response.ok(responseDTO).build();

    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylistName(@PathParam("id") int playlistId, @QueryParam("token") String token, PlayListDTO updatedPlaylist) {
        String currentUser = authenticate(token);

        boolean updated = playlistService.updatePlaylistName(playlistId, updatedPlaylist.getName(), token);

        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Playlist not found or you are not the owner\"}")
                    .build();
        }
        return Response.ok(buildPlayListResponse(currentUser)).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, PlayListDTO newList) {

        String currentUser = authenticate(token);
        playlistService.addPlaylist(newList, currentUser);
        return Response.ok(buildPlayListResponse(currentUser)).build();

    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int playlistId, @QueryParam("token") String token) {
        String currentUser = authenticate(token);
        boolean deleted = playlistService.deletePlaylist(playlistId, currentUser);
        if (!deleted) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity(INVALID_TOKEN)
                    .build();
        }
        return Response.ok(buildPlayListResponse(currentUser)).build();
    }


    private PlayListResponseDTO buildPlayListResponse(String currentUser) {
        var playlists = playlistService.getAllPlayLists();
        for (PlayListDTO playlist : playlists) {
            boolean isOwner = playlistDAO.isOwner(playlist.getId(), currentUser);
            playlist.setOwner(isOwner);
        }
        int totalLength = playlists.stream()
                .flatMap(p -> p.getTracks().stream())
                .mapToInt(TrackDTO::getDuration)
                .sum();
        return new PlayListResponseDTO(playlists, totalLength);
    }

}


