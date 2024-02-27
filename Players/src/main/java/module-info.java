module assignment.players {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;
    requires javafx.media;


/*
    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
*/
    opens assignment.players to javafx.fxml;
    exports assignment.players;
}