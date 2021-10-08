package Dict;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller{

  private Stage stage;
  private Scene scene;
  private FXMLLoader root;
  public void functionBack(ActionEvent event) throws IOException {
    root = new FXMLLoader(Main.class.getResource("searchScene.fxml"));
    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root.load());
    stage.setScene(scene);
    stage.show();
  }
  public void gotoEditScene(ActionEvent event) throws IOException {
    root = new FXMLLoader(Main.class.getResource("editScene.fxml"));
    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root.load());
    stage.setScene(scene);
    stage.show();
  }
  public void gotoAddScene(ActionEvent event) throws IOException {
    root = new FXMLLoader(Main.class.getResource("addScene.fxml"));
    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root.load());
    stage.setScene(scene);
    stage.show();
  }
  public void gotoRemoveScene(ActionEvent event) throws IOException {
    root = new FXMLLoader(Main.class.getResource("removeScene.fxml"));
    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root.load());
    stage.setScene(scene);
    stage.show();
  }
  public void gotoAboutUsScene(ActionEvent event) throws IOException {
    root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
    scene = new Scene(root.load());
    stage.setScene(scene);
    stage.show();
  }
}
