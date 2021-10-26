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
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    private Stage stage;
    private Scene scene;
    private FXMLLoader root;

    @FXML
    private TextField Search;

    @FXML
    private JFXButton searchButton;

    @FXML
    private ListView<String> Suggest;

    @FXML
    private TextField Word;

    @FXML
    private TextField Pronounce;

    @FXML
    private JFXButton Confirm;

    @FXML
    private HTMLEditor htmlEditor;

    /*
     * ChangeListerner for Suggest ListView.
     */
    ChangeListener<String> suggestChanged = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String choosen = Suggest.getSelectionModel().getSelectedItem();
            Search.setText(choosen);
            htmlEditor.setHtmlText(InitDB.details.get(choosen));
            Pronounce.setText(InitDB.pronounce.get(choosen));
            Word.setText(choosen);
        }
    };

    @Override
    /*
      Initialize
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        if (!AddController.wordToCheck.equals("")) {
            Search.setText(AddController.wordToCheck);
            Word.setText(AddController.wordToCheck);
            htmlEditor.setHtmlText(InitDB.details.get(AddController.wordToCheck));
            Pronounce.setText(InitDB.pronounce.get(AddController.wordToCheck));
            AddController.wordToCheck = "";
        }
        ObservableList<String> contentToShow = FXCollections.observableArrayList();
        String compareText = Search.getText();
        if (!compareText.equals("")) {
            for (String s : InitDB.wordList) {
                if (s.startsWith(compareText)) {
                    contentToShow.add(s);
                    if (contentToShow.size() > 23) {
                        break;
                    }
                }
            }
        }
        Suggest.setItems(contentToShow);

        Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);
    }

    @FXML
    /*
      Confirm Button Handler
     */
    void Submit(ActionEvent event) {
        try {
            //Update Database
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/dictionary", "root", "menowa1801");
            String updateStat = "UPDATE dict " + "SET detail = ?, pronounce = ?" + "WHERE word = ?";
            PreparedStatement stat = con.prepareStatement(updateStat);
            stat.setString(1, htmlEditor.getHtmlText());
            stat.setString(2, Pronounce.getText());
            stat.setString(3, Word.getText());
            stat.executeUpdate();

            //Notify
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Warning");
            alert.setContentText(Word.getText() + " Đã được cập nhật!");
            alert.setHeaderText(null);
            alert.showAndWait();

            //Update being Used Data
            InitDB.pronounce.put(Word.getText(), Pronounce.getText());
            InitDB.details.put(Word.getText(), htmlEditor.getHtmlText());

            stat.close();
            con.close();
        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @FXML
    /*
      If Search TextField on Focus --> clear Selection
     */
    void onFocus(MouseEvent event) {
        Suggest.getSelectionModel().selectedItemProperty().removeListener(suggestChanged);
        Suggest.getSelectionModel().clearSelection();
        Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);
    }

    @FXML
    /*
      Update Suggest ListView on Key Typed
     */
    void updateOnKeyTyped(KeyEvent event) {
        ObservableList<String> contentToShow = FXCollections.observableArrayList();
        String compareText = Search.getText();
        if (!compareText.equals("")) {
            for (String s : InitDB.wordList) {
                if (s.startsWith(compareText)) {
                    contentToShow.add(s);
                    if (contentToShow.size() > 23) {
                        break;
                    }
                }
            }
        }
        Suggest.setItems(contentToShow);
    }

    @FXML
    /*
      Search Button Handler
     */
    void Searaching(ActionEvent event) {
        if (InitDB.details.containsKey(Search.getText())) {
            htmlEditor.setHtmlText(InitDB.details.get(Search.getText()));
            Pronounce.setText(InitDB.pronounce.get(Search.getText()));
            Word.setText(Search.getText());
        }
        if (!Search.getText().equals("")) {
            if (Suggest.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setContentText("Từ không tồn tại!");
                alert.setHeaderText(null);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setContentText("Ý bạn là: " + Suggest.getItems().get(0) + "?" +
                        "\nBạn có thể chọn từ ở mục gợi ý");
                alert.setHeaderText(null);
                alert.showAndWait();
            }
        }
    }

    //Switch Scene Function: not much to explain here!

    public void functionBack(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("searchScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }
    public void gotoAddScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("addScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }
    public void gotoRemoveScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("removeScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }
    public void gotoAboutUsScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }
}
