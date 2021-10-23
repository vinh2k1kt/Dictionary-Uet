package Dict;

import com.jfoenix.controls.JFXButton;
//import com.sun.speech.freetts.en.us.FeatureProcessors;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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
import java.util.Optional;
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
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);
    }

    @FXML
    void Delete(ActionEvent event) {
        if (Search.getText().equals("") || !InitDB.wordList.contains(Search.getText())) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Warning");
            alert.setContentText("Từ không tồn tại!");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            //Notify
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm");
            alert.setContentText("Bạn có chắc bạn muốn xóa " + Search.getText() + "?");
            alert.setHeaderText(null);

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                try {

                    Suggest.getSelectionModel().selectedItemProperty().removeListener(suggestChanged);
                    Suggest.getSelectionModel().clearSelection();
                    Suggest.getItems().remove(Search.getText());
                    Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);

                    //Update Database
                    Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "menowa1801");
                    String deleteStat = "DELETE FROM `dict` WHERE word = ?";
                    PreparedStatement stat = con.prepareStatement(deleteStat);
                    stat.setString(1, Search.getText());
                    stat.executeUpdate();

                    //Update being Used Data
                    InitDB.details.remove(Search.getText());
                    InitDB.wordList.remove(Search.getText());

                    stat.close();
                    con.close();

                    //Notify
                    Alert Confirm = new Alert(Alert.AlertType.INFORMATION);
                    Confirm.setTitle("Notification");
                    Confirm.setContentText("Đã xóa " + Search.getText() + " khỏi từ điển");
                    Confirm.setHeaderText(null);
                    Confirm.showAndWait();

                    //Clear Text
                    Search.clear();
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                }
            }
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
                    if (contentToShow.size() > 10) {
                        break;
                    }
                }
            }
        }
        Suggest.setItems(contentToShow);
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
    public void gotoAddScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("addScene.fxml"));
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
