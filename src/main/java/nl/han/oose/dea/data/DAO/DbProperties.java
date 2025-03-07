package nl.han.oose.dea.data.DAO;

import jakarta.enterprise.context.ApplicationScoped;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


@ApplicationScoped
public class DbProperties {
    private Logger logger = Logger.getLogger(getClass().getName());
    private Properties properties;

    public DbProperties() {
        properties = new Properties();
        try {
            properties.load(getClass().getClassLoader().getResourceAsStream("database.properties"));
        } catch (IOException e) {
            logger.log(Level.SEVERE, "Can't access property file database.properties", e);
        }
    }

    public String getDriver() {
        return properties.getProperty("driver");
    }

    public String getConnectionString() {
        return properties.getProperty("connectionstring");
    }

    public String getUser() {
        return properties.getProperty("user");
    }

    public String getPassword() {
        return properties.getProperty("password");
    }
}
