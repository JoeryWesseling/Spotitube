package nl.han.oose.dea.data.queries;

public class UserQueries {
    public static final String LOGIN_QUERY = "SELECT id, username, password FROM users WHERE username = ? AND password = ?";
    public static final String ADD_TOKEN_QUERY = "UPDATE users SET token = ? WHERE id = ?";
    public static final String FETCH_USER_BY_TOKEN_QUERY = "SELECT id, username FROM users WHERE token = ?";


}
