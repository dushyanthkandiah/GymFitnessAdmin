package ServerLink;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;


public class Database {
    //    public static String connectionString = "jdbc:mysql://den1.mysql1.gear.host/gymfitness";
    public static final String db_user = "dushyanth_exclusive";
    public static final String db_pass = "dushyanth@123";
    public static final String DRIVER = "com.mysql.jdbc.Driver";
    public static String connectionString = "jdbc:mysql://192.168.1.250:3306/gymfitness";
//    public static final String db_user = "gymfitness";
    private static Connection conn = null;
    private static Statement st = null;

    public Database() {

        try {
            Class.forName(DRIVER);
            conn = DriverManager.getConnection(connectionString, db_user, db_pass);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Connection getConn() {
        return conn;
    }

    public ResultSet executeQuery(String sql) {
        ResultSet res = null;
        try {

            st = conn.createStatement();
            res = st.executeQuery(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return res;

    }

    public PreparedStatement executeUpdate(String sql) {
        PreparedStatement ps = null;
        try {
            ps = conn.prepareStatement(sql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ps;
    }


}
