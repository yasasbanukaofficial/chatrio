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

import static java.lang.Thread.sleep;

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
    private volatile boolean running = false;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        connectToServer();
    }

    private void connectToServer() {
        new Thread(() -> {
            running = true;
            while (running) {
                if (socket == null || socket.isClosed()) {
                    try {
                        socket = new Socket("localhost", 3000);
                        dOS = new DataOutputStream(socket.getOutputStream());
                        dIS = new DataInputStream(socket.getInputStream());

                        Platform.runLater(() -> displayMsg("Connected to Server", "sys"));

                        listenForMessages();
                    } catch (IOException e) {
                        Platform.runLater(() -> displayMsg("Server Disconnected", "sys"));
                        sleep(2000);
                    }
                } else {
                    sleep(2000);
                }
            }
        }).start();
    }

    private void listenForMessages() {
        new Thread(() -> {
            try {
                while (running && socket != null && !socket.isClosed()) {
                    String msg = dIS.readUTF();
                    Platform.runLater(() -> displayMsg(msg, "server"));
                }
            } catch (IOException e) {
                Platform.runLater(() -> displayMsg("Server Disconnected", "sys"));
                cleanup();
            }
        }).start();
    }

    public void sendMsg(MouseEvent mouseEvent) {
        try {
            String message = msgInput.getText();
            if (dOS != null) {
                dOS.writeUTF(message);
                dOS.flush();
                displayMsg(message, "client");
                msgInput.clear();
            } else {
                displayMsg("Not connected to server", "sys");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayMsg(String inputMsg, String sender) {
        HBox bubble = new HBox();
        Label msg = new Label(inputMsg);
        msg.setWrapText(true);
        msg.setStyle("-fx-background-color: #4a90e2; -fx-text-fill: white; -fx-padding: 8 12; -fx-background-radius: 15;");
        bubble.getChildren().add(msg);
        bubble.setAlignment(
                sender.equalsIgnoreCase("client") ? Pos.BASELINE_RIGHT :
                        sender.equalsIgnoreCase("server") ? Pos.BASELINE_LEFT : Pos.BASELINE_CENTER
        );
        chatDisplay.getChildren().add(bubble);
    }

    public void endSession(MouseEvent mouseEvent) {
        running = false;
        cleanup();
        Platform.runLater(() -> displayMsg("Session Ended", "sys"));
    }

    private void cleanup() {
        try {
            if (dIS != null) {
                dIS.close();
                dIS = null;
            }
            if (dOS != null) {
                dOS.close();
                dOS = null;
            }
            if (socket != null && !socket.isClosed()) {
                socket.close();
            }
            socket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sleep(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException ignored) {}
    }

}
