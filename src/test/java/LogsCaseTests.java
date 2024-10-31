import static org.junit.jupiter.api.Assertions.*;

import com.example.demoplswork.model.BaseDAO;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.LogsDAO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class LogsCaseTests {

    private LogsDAO logsDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up an in-memory SQLite database for testing
        String url = "jdbc:sqlite::memory:";
        connection = DriverManager.getConnection(url);
        logsDAO = new LogsDAO();
        BaseDAO.setConnection(connection); // Use the setter method
        logsDAO.createLogsTable(); // Create the logs table in the in-memory database
    }

    @Test
    public void testInsertLog_NullLog() {
        SQLException exception = assertThrows(SQLException.class, () -> {
            logsDAO.insertLog(1, null);
        });
        assertNotNull(exception);
    }

    @Test
    public void testAddToDoItem_InvalidLogId() {
        String toDoItem = "Invalid To-Do Item";
        boolean isChecked = true;

        // Attempt to add a to-do item to a non-existent log (invalid log ID)
        SQLException exception = assertThrows(SQLException.class, () -> {
            logsDAO.addToDoItem(-1, toDoItem, isChecked);
        });
        assertNotNull(exception);
    }

    @Test
    public void testAddImage_InvalidLogId() {
        String imagePath = "path/to/invalid_image.jpg";

        // Attempt to add an image to a non-existent log (invalid log ID)
        SQLException exception = assertThrows(SQLException.class, () -> {
            logsDAO.addImage(-1, imagePath);
        });
        assertNotNull(exception);
    }

    @Test
    public void testGetLogsForUser_NoLogs() throws SQLException {
        // Attempt to retrieve logs for a user with no logs
        assertEquals(0, logsDAO.getLogsForUser(999).size());
    }

    @Test
    public void testInsertLog_InvalidUserId() {
        String logName = "Invalid User Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());

        // Attempt to insert a log for an invalid user ID
        SQLException exception = assertThrows(SQLException.class, () -> {
            logsDAO.insertLog(-1, log);
        });
        assertNotNull(exception);
    }

    @Test
    public void testUpdateToDoItemStatus_InvalidTask() throws SQLException {
        // Create a valid log
        String logName = "Test Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        int logId = logsDAO.insertLog(1, log);

        // Attempt to update a non-existent to-do item
        double progress = logsDAO.updateToDoItemStatus(logId, "Non-Existent Task", true);
        assertEquals(0.0, progress); // Progress should remain unchanged
    }
}

