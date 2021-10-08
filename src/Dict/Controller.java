package Dict;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    public InitDB initDB = new InitDB();            //Use for InitDB purpose don't delete this//

    private SearchHistory searchHistory = new SearchHistory();
    private ObservableList<String> searched = FXCollections.observableArrayList();

    @FXML
    Button button;

    @FXML
    TextField textField;

    @FXML
    ListView<String> Suggest;

    @FXML
    TextArea textArea;

    @FXML
    Label warningLabel = new Label();

    @FXML
    ListView<String> History;

    /*
     * ChangeListerner for Suggest ListView.
     */
    ChangeListener<String> changed = new ChangeListener<>() {
        @Override
        public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
            String choosen = Suggest.getSelectionModel().getSelectedItem();
            textField.setText(choosen);
            textArea.setWrapText(true);
            textArea.setText(textField.getText() + "\n" + InitDB.details.get(choosen));
            searchHistory.addWord(choosen);
            searched.clear();
            searched.addAll(SearchHistory.searchedWords);
            History.setItems(searched);
        }
    };

    public Controller() throws SQLException {
    }

    @Override
    /*
     * Initialize Suggest ListView.
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Suggest.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        Suggest.getSelectionModel().selectedItemProperty().addListener(changed);
        searchHistory.init();
        searched.addAll(SearchHistory.searchedWords);
        History.setItems(searched);
    }

    @FXML
    /*
     * If click on textField --> ClearSelection of Suggest ListVuew
     */
    void listOnClicked(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            Suggest.getSelectionModel().selectedItemProperty().removeListener(changed);
            Suggest.getSelectionModel().clearSelection();
            Suggest.getSelectionModel().selectedItemProperty().addListener(changed);
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
    public void searchButton() {
        if (!textField.getText().equals("")) {
            if (Suggest.getItems().isEmpty()) {
                warningLabel.setVisible(true);
                warningLabel.setText("Can't Find Word!");
            }
            if (InitDB.details.containsKey(textField.getText())) {
                textArea.setText(textField.getText() + "\n" + InitDB.details.get(textField.getText()));
                searchHistory.addWord(textField.getText());
                searched.clear();
                searched.addAll(SearchHistory.searchedWords);
            } else {
                warningLabel.setVisible(true);
                warningLabel.setText("Can't Find Word!");
            }
            History.setItems(searched);
        }
    }

    /*
    * Easter Egg.
     */
    @FXML
    Hyperlink Roll;

    @FXML
    public void Rick() throws URISyntaxException, IOException {
        Desktop.getDesktop().browse(new URI("https://www.youtube.com/watch?v=dQw4w9WgXcQ"));
    }

}
