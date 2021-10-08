package Dict;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Dictionary");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

        primaryStage.setOnCloseRequest(event -> {
            try {
                searchHistory.onCloseAction(primaryStage);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}
