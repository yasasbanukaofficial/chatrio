package lk.ijse.chatrio.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ClientController implements Initializable {
    public ImageView pfp;
    public ScrollPane scrollPane;
    public TextField msgInput;
    public ImageView sendBtn;
    public VBox chatDisplay;
    public Button endBtn;

    private Socket socket;
    private DataOutputStream dOS;
    private DataInputStream dIS;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 3000);
                dOS = new DataOutputStream(socket.getOutputStream());
                dIS = new DataInputStream((socket.getInputStream()));

                String msg = "";
                while ((msg = dIS.readUTF()) != null) {
                    String finalMsg = msg;
                    Platform.runLater(() -> displayMsg(finalMsg, "server"));
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    Platform.runLater(()->{
                        new Alert(Alert.AlertType.INFORMATION, "The connection has been closed.");
                    });
                }
            } finally {
                try {
                    if (dOS != null) dOS.close();
                    if (dIS != null) dIS.close();
                    if (socket != null && !socket.isClosed()) socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void sendMsg(MouseEvent mouseEvent) {
        try {
            String message = msgInput.getText();
            dOS.writeUTF(message);
            dOS.flush();
            displayMsg(message, "client");
            msgInput.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMsg(String inputMsg, String sender){
        HBox bubble = new HBox();
        Label msg = new Label(inputMsg);
        msg.setWrapText(true);
        msg.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-padding: 8 12; -fx-background-radius: 15;");
        bubble.getChildren().add(msg);
        bubble.setAlignment(sender.equalsIgnoreCase("client") ? Pos.BASELINE_RIGHT : Pos.BASELINE_LEFT);
        chatDisplay.getChildren().add(bubble);
    }

    public void endSession(MouseEvent mouseEvent) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
