package Dict;

import com.jfoenix.controls.JFXButton;
//import com.sun.speech.freetts.Voice;
//import com.sun.speech.freetts.VoiceManager;
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
import javafx.scene.web.WebView;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private ObservableList<String> searched = FXCollections.observableArrayList();
    private Stage stage;
    private Scene scene;
    private FXMLLoader root;

    @FXML
    Button search;

    @FXML
    JFXButton prounounce;

    @FXML
    TextField textField;

    @FXML
    ListView<String> Suggest;

    @FXML
    Label showPronounce;

    @FXML
    Label showWord;

    @FXML
    ListView<String> History;

    @FXML
    private WebView showDetails;


    /*
     * ChangeListerner for Suggest ListView.
     */
    ChangeListener<String> suggestChanged = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String choosen = Suggest.getSelectionModel().getSelectedItem();
            textField.setText(choosen);
            showDetails.getEngine().loadContent(InitDB.details.get(choosen));
            showPronounce.setText(InitDB.pronounce.get(choosen));
            showWord.setText(choosen);
            SearchHistory.addWord(choosen);
            searched.clear();
            searched.addAll(SearchHistory.searchedWords);
            History.setItems(searched);
        }
    };

    /**
     * ChangedListener for SearchHistory.
     */
    ChangeListener<String> historyChanged = new ChangeListener<String>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String choosen = History.getSelectionModel().getSelectedItem();
            textField.setText(choosen);
            showDetails.getEngine().loadContent(InitDB.details.get(choosen));
            showPronounce.setText(InitDB.pronounce.get(choosen));
            showWord.setText(choosen);
        }
    };

    @Override
    /*
     * Initialize.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Suggest.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);

        searched.addAll(SearchHistory.searchedWords);

        History.getSelectionModel().selectedItemProperty().addListener(historyChanged);
        History.setItems(searched);
    }

    @FXML
    /*
      Prounce Word When Pronounce Button Clicked.
     */
    void Barking(ActionEvent event) {

    }


    @FXML
        /*
         * If click on textField --> ClearSelection of Suggest & History
         */
    void listOnClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {

            //  Gotta remove changedListener before we can clearSelection.
            //  Then add changedListener again or everything gonna explode.

            Suggest.getSelectionModel().selectedItemProperty().removeListener(suggestChanged);
            Suggest.getSelectionModel().clearSelection();
            Suggest.getSelectionModel().selectedItemProperty().addListener(suggestChanged);

            History.getSelectionModel().selectedItemProperty().removeListener(historyChanged);
            History.getSelectionModel().clearSelection();
            History.getSelectionModel().selectedItemProperty().addListener(historyChanged);

        }
    }

    @FXML
    /*
      Handler Key Event On TextField & Suggest ListView.
     */
    void updateOnKeyType() {
        ObservableList<String> contentToShow = FXCollections.observableArrayList();
        String compareText = textField.getText();
        if (!compareText.equals("")) {
            for (String s : InitDB.wordList) {
                if (s.startsWith(compareText)) {
                    contentToShow.add(s);
                }
            }
        }
        Suggest.setItems(contentToShow);
    }

    @FXML
    /*
     * Handler Search Button Event
     */
    public void Searching() {
        if (!textField.getText().equals("")) {
            if (InitDB.details.containsKey(textField.getText())) {
                showDetails.getEngine().loadContent(InitDB.details.get(textField.getText()));
                showPronounce.setText(InitDB.pronounce.get(textField.getText()));
                showWord.setText(textField.getText());
                SearchHistory.addWord(textField.getText());
                searched.clear();
                searched.addAll(SearchHistory.searchedWords);
            }
            if (Suggest.getItems().isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Warning");
                alert.setContentText("Không tìm thấy từ!");
                alert.setHeaderText(null);
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Warning");
                alert.setContentText("Ý bạn là: " + Suggest.getItems().get(0) + "?" +
                        "\nBạn có thể chọn từ khác ở mục gợi ý");
                alert.setHeaderText(null);
                alert.showAndWait();
            }

            History.setItems(searched);
        }
    }

    //Switch Scene Function: not much to explain here!

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
