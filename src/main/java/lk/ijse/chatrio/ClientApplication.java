package lk.ijse.chatrio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ClientApplication extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {
        Parent serverParent = FXMLLoader.load(Objects.requireNonNull(ServerApplication.class.getResource("/view/Client.fxml")));
        Scene serverScene = new Scene(serverParent);
        Stage serverStage = new Stage();
        serverStage.setScene(serverScene);
        serverStage.show();
    }
}
