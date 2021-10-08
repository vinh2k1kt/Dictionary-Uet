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
    private String url = "jdbc:mysql://localhost:3306/dictionary";
    private String user = "root";
    private String pass = "menowa1801";
    public static Deque<String> searchedWords = new LinkedList<>();
    public static boolean changed = false;

    public SearchHistory() throws SQLException {
    }

    public void init() {
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stat = con.createStatement();
            String sql = "select*from searchedwordsDB";
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

    public void addWord(String word) {
        changed = true;
        if (searchedWords.size() >= 7) {
            searchedWords.removeLast();
            searchedWords.addFirst(word);
        } else {
            searchedWords.addFirst(word);
        }
    }

    public void updateOnClose() throws SQLException {
        Connection con = DriverManager.getConnection(url, user, pass);
        String insert = "INSERT INTO `searchedwordsDB` (word) VALUES (?)";
        PreparedStatement statement = con.prepareStatement(insert);
        for (String s : searchedWords) {
            statement.setString(1, s);
            statement.execute();
        }
        statement.close();
    }
    public void onCloseAction(Stage stage) throws SQLException {
        Connection con = DriverManager.getConnection(url, user, pass);
        Statement truncate = con.createStatement();
        String truncate_sql = "truncate table searchedwordsDB";
        truncate.executeUpdate(truncate_sql);
        this.updateOnClose();
        truncate.close();
        stage.close();
    }
}
