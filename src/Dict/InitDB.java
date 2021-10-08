package Dict;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class InitDB {
    private String url = "jdbc:mysql://localhost:3306/dictionary";
    private String user = "root";
    private String pass = "menowa1801";

    public static HashMap<String, String> details = new HashMap<>();
    public static List<String> wordList = new ArrayList<>();

    public InitDB() {
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stat = con.createStatement();
            String sql = "select*from dict";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                details.put(rs.getString("word").toLowerCase(Locale.ROOT),
                        rs.getString("detail").toLowerCase(Locale.ROOT));
                wordList.add(rs.getString("word").toLowerCase(Locale.ROOT));
            }
        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
