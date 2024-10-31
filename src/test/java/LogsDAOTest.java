import static org.junit.jupiter.api.Assertions.*;

import com.example.demoplswork.model.BaseDAO;
import com.example.demoplswork.model.LogsDAO;
import com.example.demoplswork.model.Logs;
import com.example.demoplswork.model.Material;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.sql.DriverManager;

public class LogsDAOTest {

    private LogsDAO logsDAO;
    private Connection connection;

    @BeforeEach
    public void setUp() throws SQLException {
        // Set up an in-memory SQLite database for testing
        String url = "jdbc:sqlite::memory:";
        connection = DriverManager.getConnection(url);
        logsDAO = new LogsDAO();
        BaseDAO.setConnection(connection); // Use the setter method

        logsDAO.createLogsTable();
    }


    @Test
    public void testInsertLog_Success() throws SQLException {
        String logName = "Test Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        log.setProgress(50.0);

        int logId = logsDAO.insertLog(1, log);

        assertTrue(logId > 0);
    }

    @Test
    public void testAddToDoItem() throws SQLException {
        String logName = "Test Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        int logId = logsDAO.insertLog(1, log);

        String toDoItem = "Test To-Do Item";
        boolean isChecked = false;

        logsDAO.addToDoItem(logId, toDoItem, isChecked);

        List<Object[]> logs = logsDAO.getLogsForUser(1);
        Logs retrievedLog = (Logs) logs.get(0)[1];
        assertEquals(1, retrievedLog.getToDoItems().size());
        assertEquals(toDoItem, retrievedLog.getToDoItems().get(0).getKey());
        assertEquals(isChecked, retrievedLog.getToDoItems().get(0).getValue());
    }

    @Test
    public void testAddImage() throws SQLException {
        String logName = "Test Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        int logId = logsDAO.insertLog(1, log);

        String imagePath = "path/to/image.jpg";
        logsDAO.addImage(logId, imagePath);

        List<Object[]> logs = logsDAO.getLogsForUser(1);
        Logs retrievedLog = (Logs) logs.get(0)[1];
        assertEquals(1, retrievedLog.getImages().size());
        assertEquals(imagePath, retrievedLog.getImages().get(0));
    }

    @Test
    public void testAddMaterial() throws SQLException {
        String logName = "Test Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        int logId = logsDAO.insertLog(1, log);

        Material material = new Material("Wood", 5, 20.0);
        logsDAO.addMaterial(logId, material);

        List<Object[]> logs = logsDAO.getLogsForUser(1);
        Logs retrievedLog = (Logs) logs.get(0)[1];
        assertEquals(1, retrievedLog.getMaterials().size());
        assertEquals(material.getName(), retrievedLog.getMaterials().get(0).getName());
    }

    @Test
    public void testGetLogsForUser() throws SQLException {
        String logName = "Test Log";
        Logs log = new Logs(logName, new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        log.setProgress(50.0);
        int logId = logsDAO.insertLog(1, log);

        List<Object[]> logs = logsDAO.getLogsForUser(1);

        assertEquals(1, logs.size());
        assertEquals(logId, logs.get(0)[0]); // log ID
        Logs retrievedLog = (Logs) logs.get(0)[1];
        assertEquals(logName, retrievedLog.getLogName());
        assertEquals(50.0, retrievedLog.getProgress());
    }
}



