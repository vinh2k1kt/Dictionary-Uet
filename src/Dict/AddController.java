package Dict;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddController {

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
    void Submit(ActionEvent event) {
        System.out.println(htmlEditor.getHtmlText());
        try {
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "menowa1801");
            PreparedStatement stat = con.prepareStatement("INSERT INTO `dict` (word, detail, pronounce) VALUES (?, ?, ?)");
            stat.setString(1, Word.getText());
            stat.setString(2, htmlEditor.getHtmlText());
            stat.setString(3, Pronounce.getText());
            stat.execute();
            stat.close();
            con.close();
        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    //Switch Scene Function: not much to explain here!

    @FXML
    void functionBack(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("searchScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoAboutUsScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoEditScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("editScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void gotoRemoveScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("removeScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }
}
