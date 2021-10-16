package Dict;

import com.jfoenix.controls.JFXButton;
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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class RemoveController implements Initializable {

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;

    @FXML
    private TextField Search;

    @FXML
    private JFXButton Confirm;

    @FXML
    private ListView<String> Suggest;

    /*
     * ChangeListerner for Suggest ListView.
     */
    ChangeListener<String> suggestChanged = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String choosen = Suggest.getSelectionModel().getSelectedItem();
            Search.setText(choosen);
        }
    };

    /**
     * Init Changed Listener.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);
    }

    @FXML
    void Delete(ActionEvent event) {
        try {
            Suggest.getSelectionModel().selectedItemProperty().removeListener(suggestChanged);
            Suggest.getSelectionModel().clearSelection();
            Suggest.getItems().remove(Search.getText());
            Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);

            //Update Database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "menowa1801");
            String deleteStat = "DELETE FROM dict WHERE word = ?";
            PreparedStatement stat = con.prepareStatement(deleteStat);
            stat.setString(1, Search.getText());
            stat.executeUpdate();

            //Update being Used Data
            InitDB.details.remove(Search.getText());
            InitDB.wordList.remove(Search.getText());

            //Clear Text
            Search.clear();

            stat.close();
            con.close();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    /**
     * If Search TextField on Focus --> clear Selection
     */
    void onFocus(MouseEvent event) {
        Suggest.getSelectionModel().selectedItemProperty().removeListener(suggestChanged);
        Suggest.getSelectionModel().clearSelection();
        Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);
    }

    @FXML
    /**
     * Update Suggest ListView on Key Typed
     */
    void updateOnKeyTyped(KeyEvent event) {
        ObservableList<String> contentToShow = FXCollections.observableArrayList();
        String compareText = Search.getText();
        if (!compareText.equals("")) {
            for (String s : InitDB.wordList) {
                if (s.startsWith(compareText)) {
                    contentToShow.add(s);
                }
            }
        }
        Suggest.setItems(contentToShow);
    }

    //Switch Scene Function: not much to explain here!

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

    public void gotoAboutUsScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        stage.show();
    }
}
