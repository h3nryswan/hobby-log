module com.example.demoplswork {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires javafx.media;
    requires software.amazon.awssdk.auth;
    requires software.amazon.awssdk.regions;
    requires software.amazon.awssdk.services.s3;

    opens com.example.demoplswork to javafx.fxml;
    exports com.example.demoplswork;
    exports com.example.demoplswork.controller;
    opens com.example.demoplswork.controller to javafx.fxml;
    exports com.example.demoplswork.model;
    opens com.example.demoplswork.model to javafx.fxml;
    exports com.example.demoplswork.events;
    opens com.example.demoplswork.events to javafx.fxml;
}