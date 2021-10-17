package Dict;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddController {

    public static String wordToCheck = "";

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;

    @FXML
    private TextField Pronounce;

    @FXML
    private JFXButton confirm;

    @FXML
    private TextField Word;

    @FXML
    private HTMLEditor htmlEditor;


    @FXML
    /**
     * Confirm Button Handler
     */
    void Submit(ActionEvent event) throws IOException {
        if (InitDB.wordList.contains(Word.getText())) {
            wordToCheck = Word.getText();

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Word already exists!\nPlease Move To Edit Word");
            alert.setHeaderText(null);
            alert.showAndWait();
            this.gotoEditScene(event);
        } else {
            try {

                //Update Database
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "menowa1801");
                PreparedStatement stat = con.prepareStatement("INSERT INTO `check` (word, detail, pronounce) VALUES (?, ?, ?)");
                stat.setString(1, Word.getText());
                stat.setString(2, htmlEditor.getHtmlText());
                stat.setString(3, Pronounce.getText());
                stat.executeUpdate();
                stat.close();
                con.close();

                //Update Being Used Data
                InitDB.wordList.add(Word.getText());
                InitDB.details.put(Word.getText(), htmlEditor.getHtmlText());
                InitDB.pronounce.put(Word.getText(), Pronounce.getText());

                //Notify
                Alert Confirm = new Alert(Alert.AlertType.INFORMATION);
                Confirm.setTitle("Notification");
                Confirm.setContentText(Word.getText() + " Đã được thêm vào từ điển");
                Confirm.setHeaderText(null);
                Confirm.showAndWait();

            } catch (
                    SQLException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    /**
     * Check if the Word already exsist or not!
     * @param event
     */
    @FXML
    void checkWord(MouseEvent event) throws IOException {
        if (InitDB.wordList.contains(Word.getText())) {
            wordToCheck = Word.getText();

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("Từ không tồn tại!\nXin hãy chuyển sang mục sửa từ");
            alert.setHeaderText(null);
            alert.showAndWait();

            //Switch to Edit Scene
            root = new FXMLLoader(Main.class.getResource("editScene.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root.load());
            stage.setScene(scene);
            stage.show();
        }
    }

    //Switch Scene Function: not much to explain here!

    public void functionBack(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("searchScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.show();
    }
    public void gotoEditScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("editScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.show();
    }
    public void gotoRemoveScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("removeScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.show();
    }
    public void gotoAboutUsScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("style.css");
        stage.show();
    }
}
