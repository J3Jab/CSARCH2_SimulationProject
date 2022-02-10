package converter;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/converter.fxml"));
        primaryStage.setTitle("Decimal to Binary and Hexadecimal Converter");
        primaryStage.setScene(new Scene(root, 783, 604, Color.web("#F6F6F6")));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
