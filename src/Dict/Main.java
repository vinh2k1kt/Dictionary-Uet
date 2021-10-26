package Dict;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.sql.SQLException;

public class Main extends Application {
    private SearchHistory searchHistory;

    {
        try {
            searchHistory = new SearchHistory();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SearchHistory.init();
        InitDB.init();
        Parent root = FXMLLoader.load(getClass().getResource("searchScene.fxml"));
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Dict/image/dictionary.png"));
        root.getStylesheets().add("/style.css");
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
