package nl.han.oose.dea.resources;


import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import nl.han.oose.dea.DTO.PlayListDTO;
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
    public Response getPlayLists(@QueryParam("token") String token) {

        if (token == null || !tokenService.isValidToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Missing token\"}")
                    .build();
        }

        var playlists = playlistService.getAllPlayLists();
        int totalLength = playlists.stream().flatMap(p -> p.getTracks().stream()).mapToInt(TrackDTO::getDuration).sum();

        return Response.ok(new PlayListResponseDTO(playlists, totalLength)).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updatePlaylistName(@PathParam("id") int playlistId, @QueryParam("token") String token, PlayListDTO updatedPlaylist) {
        if (token == null || !tokenService.isValidToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }
        boolean updated = playlistService.updatePlaylistName(playlistId, updatedPlaylist.getName(), token);

        if (!updated) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Playlist not found or you are not the owner\"}")
                    .build();
        }
        return Response.ok().build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addPlaylist(@QueryParam("token") String token, PlayListDTO newList) {
        if (token == null || !tokenService.isValidToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }
        String username = tokenService.getUsernameFromToken(token);

        playlistService.addPlaylist(newList, username);

        var updatedPlaylist = playlistService.getAllPlayLists();
        int totalLength = updatedPlaylist.stream()
                .flatMap(p -> p.getTracks().stream())
                .mapToInt(TrackDTO::getDuration).sum();
        return Response.ok(new PlayListResponseDTO(updatedPlaylist, totalLength)).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deletePlaylist(@PathParam("id") int playlistId, @QueryParam("token") String token) {

        if (token == null || !tokenService.isValidToken(token)) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }
        String username = tokenService.getUsernameFromToken(token);

        boolean deleted = playlistService.deletePlaylist(playlistId, username);
        if (!deleted) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\": \"Invalid or missing token\"}")
                    .build();
        }
        var updatedPlaylist = playlistService.getAllPlayLists();
        int totalLength = updatedPlaylist.stream()
                .flatMap(p -> p.getTracks().stream())
                .mapToInt(TrackDTO::getDuration).sum();
        return Response.ok(new PlayListResponseDTO(updatedPlaylist, totalLength)).build();
    }
}


