package Dict;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class aboutUs {

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;

    /*
     * Easter Egg.
     */
    @FXML
    Hyperlink Roll;

    @FXML
    public void Rick() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
    }

    public void functionBack(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("searchScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    public void gotoEditScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("editScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    public void gotoAddScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("addScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    public void gotoRemoveScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("removeScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }

    public void gotoAboutUsScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }
}
