module Workshop {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;
    requires java.sql;
    requires mysql.connector.j;

    opens controllers to javafx.fxml;
    opens entities to javafx.base;
    
    exports main;
}
