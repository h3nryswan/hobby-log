package com.example.demoplswork.events;

import java.sql.SQLException;
import java.util.List;

/**
 * ILogEventDAO interface defines the methods for interacting with log events in the database.
 * It has a method to insert a log event into the database.
 * It has a method to retrieve log events for a specific log ID.
 */
public interface ILogEventDAO {
    /**
     * Inserts a log event into the database.
     * @param event The log event to insert.
     * @throws SQLException If an error occurs while inserting the log event.
     */
    void insertLogEvent(LogEvent event) throws SQLException;
    List<LogEvent> getLogEventsForLog(int logId) throws SQLException;
}

