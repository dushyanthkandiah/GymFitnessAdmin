package ServerLink;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassMessages;

public class ServerMessage {

    private Database db;
    private ClassMessages classMessages;
    private ArrayList<ClassMessages> list;

    public ServerMessage() {
        db = new Database();
        classMessages = new ClassMessages();
        list = new ArrayList<>();
    }

    public String getAllRecords(int limit) {
        String flag;

        int limitRate = 15;

        String sql = "select * from messages where cus_id = '" + classMessages.getCustomerId() + "' order by msg_id desc limit " + (limit * limitRate) + ", " + limitRate + "";

//                System.out.println(sql + " ******");

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassMessages(
                            rs.getInt("msg_id"),
                            rs.getInt("cus_id"),
                            rs.getString("message"),
                            rs.getString("date_time"),
                            rs.getString("message_owner")
                    ));
                }


            } catch (SQLException e) {
                e.printStackTrace();
                flag = "nodata";
            }


        } else {
            flag = "failed";
        }

        return flag;
    }

    public int Save() {
        int result = 0;
        PreparedStatement st;

        try {

            st = db.executeUpdate("INSERT INTO messages " +
                    "(cus_id, " +
                    "message) " +
                    "VALUES(?, ?)");

            st.setInt(1, classMessages.getCustomerId());
            st.setString(2, classMessages.getMessage());
            System.out.println(st);

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public ClassMessages getClassMessages() {
        return classMessages;
    }

    public ArrayList<ClassMessages> getList() {
        return list;
    }
}
