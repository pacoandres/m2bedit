module org.gnu.itsmoroto.m2bedit {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires kotlin.stdlib;
    requires mathcat4j.core;
    requires snuggletex.core;
    requires liblouis.java;
    
    opens org.gnu.itsmoroto.m2bedit to javafx.fxml;
    exports org.gnu.itsmoroto.m2bedit;
}