package lk.ijse.chatrio;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Objects;

public class ServerApplication extends Application{
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Parent clientParent = FXMLLoader.load(Objects.requireNonNull(ServerApplication.class.getResource("/view/Server.fxml")));
        Scene scene = new Scene(clientParent);
        stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }
}
