package Dict;

import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Deque;
import java.util.LinkedList;

public class SearchHistory {
    private static String url = "jdbc:mysql://localhost:3306/dictionary";
    private static String user = "root";
    private static String pass = "menowa1801";
    public static Deque<String> searchedWords = new LinkedList<>();
    public static boolean changed = false;

    public SearchHistory() throws SQLException {
    }

    /**
     * Load Search History from database
     */
    public static void init() {
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stat = con.createStatement();
            String sql = "select*from searchhistory";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                searchedWords.add(rs.getString("word"));
            }
            stat.close();
        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Add new word to Search History.
     * @param word word need to be added.
     */
    public static void addWord(String word) {
        changed = true;
        if (searchedWords.contains(word)) {
            searchedWords.remove(word);
            searchedWords.addFirst(word);
        } else if (searchedWords.size() >= 7) {
            searchedWords.removeLast();
            searchedWords.addFirst(word);
        } else {
            searchedWords.addFirst(word);
        }
    }

    /*
    Update Search History in Database
     */
    public static void updateOnClose() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, pass);
        String insert = "INSERT INTO `searchhistory` (word) VALUES (?)";
        PreparedStatement statement = con.prepareStatement(insert);
        for (String s : searchedWords) {
            statement.setString(1, s);
            statement.execute();
        }
        statement.close();
    }

    /**
     * Updates everything before closing app.
     * @param stage main Window.
     */
    public static void onCloseAction(Stage stage) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement truncate = con.createStatement();
        String truncate_sql = "truncate table searchhistory";
        truncate.executeUpdate(truncate_sql);
        updateOnClose();
        truncate.close();
        stage.close();
    }
}
