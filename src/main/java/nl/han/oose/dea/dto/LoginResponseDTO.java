package nl.han.oose.dea.dto;

public class LoginResponseDTO {

    private int id;
    private String token;

    private String user;


    public LoginResponseDTO(int id, String token, String user){
        this.id = id;
        this.token = token;
        this.user = user;
    }

    public LoginResponseDTO(){

    }

    public String getToken(){
        return token;
    }


    public void setToken(String token) {
        this.token = token;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
