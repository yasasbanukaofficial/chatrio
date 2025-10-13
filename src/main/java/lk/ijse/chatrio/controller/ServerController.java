package lk.ijse.chatrio.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
    public ImageView pfp;
    public ScrollPane scrollPane;
    public TextField msgInput;
    public ImageView sendBtn;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void sendMsg(MouseEvent mouseEvent) {
    }
}
