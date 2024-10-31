import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class DatabaseTest {
    @Test
    public void testConnection() {
        try {
            Connection conn = DriverManager.getConnection("jdbc:sqlite:contacts.db");
            assertNotNull(conn);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }
}