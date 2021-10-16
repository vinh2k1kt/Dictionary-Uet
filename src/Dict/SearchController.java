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
import java.sql.SQLException;
import java.util.ResourceBundle;

public class SearchController implements Initializable {
    private ObservableList<String> searched = FXCollections.observableArrayList();
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

    @FXML
    Button search;

    @FXML
    JFXButton prounounce;

    @FXML
    TextField textField;

    @FXML
    ListView<String> Suggest;

    @FXML
    TextArea showPronounce;

    @FXML
    TextArea showWord;

    @FXML
    Label warningLabel = new Label();

    @FXML
    ListView<String> History;

    @FXML
    private WebView showDetails;


    public SearchController() throws SQLException {
    }

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
    /**
     * Prounce Word When Pronounce Button Clicked.
     */
    void Barking(ActionEvent event) {
        System.setProperty("freetts.voices", "com.sun.speech.freetts.en.us.cmu_us_kal.KevinVoiceDirectory");
        for (Voice voice : VoiceManager.getInstance().getVoices()) {
            System.out.println(voice.getName());
        }
        Voice voice = VoiceManager.getInstance().getVoice("kevin16");
        if (voice != null) {
            voice.allocate();
            try {
                voice.setRate(170);
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

            warningLabel.setVisible(false);
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
                    warningLabel.setText("");
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
            if (Suggest.getItems().isEmpty()) {
                warningLabel.setVisible(true);
                warningLabel.setText("Can't Find Word!");
            }
            if (InitDB.details.containsKey(textField.getText())) {
                showDetails.getEngine().loadContent(InitDB.details.get(textField.getText()));
                showPronounce.setText(InitDB.pronounce.get(textField.getText()));
                showWord.setText(textField.getText());
                SearchHistory.addWord(textField.getText());
                searched.clear();
                searched.addAll(SearchHistory.searchedWords);
            } else {
                warningLabel.setVisible(true);
                warningLabel.setText("Can't Find Word!");
            }
            History.setItems(searched);
        }
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
