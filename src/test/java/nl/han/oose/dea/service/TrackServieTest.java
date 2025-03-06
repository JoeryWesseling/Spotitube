package nl.han.oose.dea.service;

import nl.han.oose.dea.data.DAO.TrackDAO;
import nl.han.oose.dea.DTO.TrackDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class TrackServieTest {

    @Mock
    private TrackDAO trackDAO;

    @InjectMocks
    private TrackService trackService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllByPlayListsFilteredCorrectly(){
        //arrange
        List<TrackDTO> allTracks = List.of(
                new TrackDTO(1,"Track1","arist",200,"album"),
                new TrackDTO(2,"Track2","arist",200,"album"),
                new TrackDTO(3,"Track3","arist",200,"album")
        );

        when(trackDAO.getAllTracks()).thenReturn(allTracks);
        //act
        List<TrackDTO> result = trackService.getAllByPlaylists(2);

        //assert
        assertEquals(1,result.size());
        assertEquals("Track2",result.get(0).getTitle());

    }
}
