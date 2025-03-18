package nl.han.oose.dea.data.database;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class DbPropertiesTest {

    private DbProperties dbProperties;

    @BeforeEach
    void setUp() {
        // Instantiate DbProperties. Its constructor will attempt to load "database.properties"
        // from the classpath, but we will override the loaded Properties with our own.
        dbProperties = new DbProperties();
    }

    @Test
    void testGettersReturnConfiguredValues() throws Exception {
        // Arrange: create a Properties object with test values.
        Properties testProps = new Properties();
        testProps.setProperty("driver", "testDriver");
        testProps.setProperty("connectionstring", "jdbc:test:database");
        testProps.setProperty("user", "testUser");
        testProps.setProperty("password", "testPassword");

        // Use reflection to inject our test properties into the dbProperties instance.
        Field propertiesField = DbProperties.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        propertiesField.set(dbProperties, testProps);

        // Act & Assert: verify that the getters return the values we injected.
        assertEquals("testDriver", dbProperties.getDriver());
        assertEquals("jdbc:test:database", dbProperties.getConnectionString());
        assertEquals("testUser", dbProperties.getUser());
        assertEquals("testPassword", dbProperties.getPassword());
    }

    @Test
    void testGettersReturnNullWhenPropertiesAreEmpty() throws Exception {
        // Arrange: create an empty Properties object.
        Properties emptyProps = new Properties();

        // Inject the empty properties into the dbProperties instance.
        Field propertiesField = DbProperties.class.getDeclaredField("properties");
        propertiesField.setAccessible(true);
        propertiesField.set(dbProperties, emptyProps);

        // Act & Assert: Since the properties are empty, getters should return null.
        assertNull(dbProperties.getDriver());
        assertNull(dbProperties.getConnectionString());
        assertNull(dbProperties.getUser());
        assertNull(dbProperties.getPassword());
    }
}
