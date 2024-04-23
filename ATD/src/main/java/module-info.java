module com.example.atd {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires com.google.gson;

    opens com.example.atd to javafx.fxml;
    exports com.example.atd;
    exports com.example.atd.adapter;
    opens com.example.atd.adapter to javafx.fxml;
    exports com.example.atd.model;
    opens com.example.atd.model to javafx.fxml;
}