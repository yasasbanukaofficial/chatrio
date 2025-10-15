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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class ServerController implements Initializable {
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
            try (ServerSocket serverSocket = new ServerSocket(3000)) {
                socket = serverSocket.accept();
                dOS = new DataOutputStream(socket.getOutputStream());
                dIS = new DataInputStream(socket.getInputStream());

                while (!socket.isClosed()) {
                    String msg = dIS.readUTF();
                    Platform.runLater(() -> displayMsg(msg, "client"));
                }
            } catch (IOException e) {
                if (!socket.isClosed()) {
                    Platform.runLater(() -> displayMsg("Client Disconnected", "sys"));
                }
                e.printStackTrace();
            } finally {
                try {
                    if (dIS != null) dIS.close();
                    if (dOS != null) dOS.close();
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
            displayMsg(message, "server");
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
        bubble.setAlignment(sender.equalsIgnoreCase("client") ? Pos.BASELINE_LEFT : sender.equalsIgnoreCase("server") ? Pos.BASELINE_RIGHT : Pos.BASELINE_CENTER);
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
