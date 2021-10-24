package Dict;

import com.jfoenix.controls.JFXButton;
import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;
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
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private ObservableList<String> searched = FXCollections.observableArrayList();
    private Stage stage;
    private Scene scene;
    private FXMLLoader root;

    private static String url = "jdbc:mysql://localhost:3306/dictionary";
    private static String user = "root";
    private static String pass = "menowa1801";
    private final Connection con = DriverManager.getConnection(url, user, pass);

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
    HTMLEditor showDetails;


    /*
     * ChangeListerner for Suggest ListView.
     */
    ChangeListener<String> suggestChanged = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String choosen = Suggest.getSelectionModel().getSelectedItem();
            textField.setText(choosen);
            showDetails.setHtmlText(InitDB.details.get(choosen));
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
            showDetails.setHtmlText(InitDB.details.get(choosen));
            showPronounce.setText(InitDB.pronounce.get(choosen));
            showWord.setText(choosen);
        }
    };

    public SearchController() throws SQLException {
    }

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

        // Hiding HTML Editor Tool-Bar
        Node[] nodes = showDetails.lookupAll(".tool-bar").toArray(new Node[0]);
        for (Node node : nodes) {
            node.setVisible(false);
            node.setManaged(false);
        }
    }

    @FXML
    /*
      Prounce Word When Pronounce Button Clicked.
     */
    void Barking(ActionEvent event) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            try {
                voice.setRate(140);
                voice.setPitch(100);
                voice.setVolume(100);
                voice.speak(showWord.getText());
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            throw new IllegalStateException("Can't Find voice:");
        }
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
    void updateOnKeyType() throws SQLException {
        ObservableList<String> contentToShow = FXCollections.observableArrayList();
        String compareText = textField.getText();
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

    @FXML
    /*
     * Handler Search Button Event
     */
    public void Searching() {
        if (!textField.getText().equals("")) {
            if (InitDB.details.containsKey(textField.getText())) {
                showDetails.setHtmlText(InitDB.details.get(textField.getText()));
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
                if (!InitDB.details.containsKey(textField.getText()))
                {
                    for (String s : InitDB.wordList) {
                        if (s.startsWith(textField.getText())) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Warning");
                            alert.setContentText("Ý bạn là: " + s + "?" +
                                    "\nBạn có thể chọn từ khác ở mục gợi ý");
                            alert.setHeaderText(null);
                            alert.showAndWait();
                            break;
                        }
                    }
                }
            }

            History.setItems(searched);
        }
    }

    //Switch Scene Function: not much to explain here!

    public void gotoEditScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("editScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }

    public void gotoAddScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("addScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }

    public void gotoRemoveScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("removeScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }

    public void gotoAboutUsScene(ActionEvent event) throws IOException {
        root = new FXMLLoader(Main.class.getResource("aboutusScene.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root.load());
        stage.setScene(scene);
        scene.getStylesheets().add("/style.css");
        stage.show();
    }
}
