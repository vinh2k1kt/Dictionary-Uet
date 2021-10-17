package Dict;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        SearchHistory.init();
        InitDB.init();
        Parent root = FXMLLoader.load(getClass().getResource("searchScene.fxml"));
        primaryStage.setTitle("Dictionary");
        Scene scene = new Scene(root);
        scene.getStylesheets().add("style.css");
        primaryStage.setScene(scene);
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                SearchHistory.onCloseAction(primaryStage);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
