package nl.han.oose.dea.DTO;

public class TrackDTO {

    private int id;
    private String title;
    private String performer;
    private int duration;
    private String album;
    private int playCount;
    private String publicationDate;
    private String description;



    public TrackDTO(){

    }

    public TrackDTO(int id, String title,String performer, int duration, String album){
        this.id = id;
        this.title = title;
        this.performer = performer;
        this.duration = duration;
        this.album = album;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPerformer() {
        return performer;
    }

    public void setPerformer(String performer) {
        this.performer = performer;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }
}
