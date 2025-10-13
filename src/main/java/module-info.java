module lk.ijse.chatrio {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;


    opens lk.ijse.chatrio.controller to javafx.fxml;
    exports lk.ijse.chatrio;
}