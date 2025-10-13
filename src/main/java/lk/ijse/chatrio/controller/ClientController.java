package lk.ijse.chatrio.controller;

import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
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
