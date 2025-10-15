package lk.ijse.chatrio.controller;

import javafx.application.Platform;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.Image;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.nio.file.Files;
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
    private ImageView imageView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try (ServerSocket serverSocket = new ServerSocket(3000)) {
                socket = serverSocket.accept();
                dOS = new DataOutputStream(socket.getOutputStream());
                dIS = new DataInputStream(socket.getInputStream());

                while (!socket.isClosed()) {
                    String msg = dIS.readUTF();
                    if (msg.equals("IMAGE")) {
                        int length= dIS.readInt();
                        byte[] imageBytes=new byte[length];
                        dIS.readFully(imageBytes);
                        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                        Platform.runLater(() ->  displayImg(bais));
                    } else {
                        Platform.runLater(() -> displayMsg(msg, "client"));
                    }
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
    
    private void displayImg(ByteArrayInputStream fileContent){
        HBox imageContainer = new HBox();
        Image img = new Image(fileContent);
        ImageView imageView = new ImageView();
        imageView.setImage(img);
        imageView.setFitHeight(200);
        imageView.setFitWidth(200);
        imageView.setPreserveRatio(true);
        imageContainer.getChildren().add(imageView);
        imageContainer.setAlignment(Pos.BASELINE_LEFT);
        imageContainer.setStyle("-fx-padding: 10;");
        chatDisplay.getChildren().add(imageContainer);
    }

    public void endSession(MouseEvent mouseEvent) {
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void addImg(MouseEvent mouseEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Image");
        File selectedFile = fileChooser.showOpenDialog(new Stage());
        try {
            byte [] fileContent = Files.readAllBytes(selectedFile.toPath());
            dOS = new DataOutputStream(socket.getOutputStream());
            dOS.writeUTF("IMAGE");
            dOS.writeInt(fileContent.length);
            dOS.write(fileContent);
            dOS.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
