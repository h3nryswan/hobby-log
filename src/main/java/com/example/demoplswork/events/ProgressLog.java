package com.example.demoplswork.events;

import java.util.ArrayList;
import java.util.List;
/**
 * ProgressLog class manages a list of log events.
 * It has a field to store the list of LogEvent objects.
 * It has a constructor to initialize the list of events.
 * It has a method to add a new event to the log.
 * It has a method to retrieve the entire log as a formatted string.
 */
public class ProgressLog {
    private List<LogEvent> events;
    /**
     * Constructor to initialize the list of events
     */
    public ProgressLog() {
        this.events = new ArrayList<>();
    }
    /**
     * Method to add a new event to the log
     * @param event
     */
    // Method to add a new event to the log
    public void addEvent(LogEvent event) {
        events.add(event);
    }
    /**
     * Method to retrieve the entire log as a formatted string
     * @return
     */
    // Method to retrieve the entire log as a formatted string
    public String getLog() {
        StringBuilder log = new StringBuilder();
        for (LogEvent event : events) {
            log.append(event.toString()).append("\n");
        }
        return log.toString();
    }


}

