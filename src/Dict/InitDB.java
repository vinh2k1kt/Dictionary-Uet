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
    private static String url = "jdbc:mysql://localhost:3306/dictionary";
    private static String user = "root";
    private static String pass = "menowa1801";

    public static HashMap<String, String> details = new HashMap<>();
    public static HashMap<String, String> pronounce = new HashMap<>();
    public static List<String> wordList = new ArrayList<>();

    public static void init() {
        try {
            Connection con = DriverManager.getConnection(url, user, pass);
            Statement stat = con.createStatement();
            String sql = "select*from `check`";
            ResultSet rs = stat.executeQuery(sql);
            while (rs.next()) {
                pronounce.put(rs.getString("word").toLowerCase(Locale.ROOT),
                        rs.getString("pronounce"));
                details.put(rs.getString("word").toLowerCase(Locale.ROOT),
                        rs.getString("detail"));
                wordList.add(rs.getString("word").toLowerCase(Locale.ROOT));
            }
        } catch (
                SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}
