package nl.han.oose.dea.data.DAO;


import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TokenDAO {
    private static final String TOKEN = "Sauron";


    public TokenDAO(){
    }

    public boolean isValidToken(String token){
        return TOKEN.equals(token);
    }

    public String getToken(){
        return TOKEN;
    }


}
