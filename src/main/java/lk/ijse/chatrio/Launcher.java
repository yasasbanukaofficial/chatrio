package lk.ijse.chatrio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class Launcher extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent clientParent = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource("/view/Client.fxml")));
        Scene scene = new Scene(clientParent);
        stage = new Stage();
        stage.setScene(scene);
        stage.show();

        Parent serverParent = FXMLLoader.load(Objects.requireNonNull(Launcher.class.getResource("/view/Server.fxml")));
        Scene serverScene = new Scene(serverParent);
        Stage serverStage = new Stage();
        serverStage.setWidth(stage.getWidth());
        serverStage.setHeight(stage.getHeight());
        serverStage.setScene(serverScene);
        serverStage.show();
    }
}
