package ServerLink;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassCustomers;
import GettersAndSetters.ClassMessages;

public class ServerMessage {

    private Database db;
    private ClassMessages classMessages;
    private ArrayList<ClassMessages> list;

    public ServerMessage() {
        classMessages = new ClassMessages();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        db = new Database();
        String flag;
        int maxLimit = 12;

        String sql = "SELECT messages.msg_id, messages.cus_id, messages.message, messages.date_time, messages.message_owner, customers.name, customers.picture " +
                "FROM messages inner join customers on messages.cus_id = customers.cus_id " +
                "WHERE messages.msg_id IN ( " +
                "SELECT MAX(messages.msg_id) " +
                "FROM messages " +
                "GROUP BY messages.cus_id " +
                ") and customers.name like '%" + searchInput + "%' order by messages.msg_id desc limit " + (limit * maxLimit) + ", " + maxLimit;

//        System.out.println(sql + " *********************");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
                    ClassMessages classMessages = new ClassMessages();
                    classMessages.setMessageId(rs.getInt("msg_id"));
                    classMessages.setCustomerId(rs.getInt("cus_id"));
                    classMessages.setMessage(rs.getString("message"));
                    classMessages.setDateTime(rs.getString("date_time"));
                    classMessages.setMessageOwner(rs.getString("message_owner"));
                    classMessages.setCustomerName(rs.getString("name"));
                    classMessages.setPicture(rs.getBytes("picture"));

                    list.add(classMessages);

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

    public String getAllRecords(int limit) {
        db = new Database();
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
        db = new Database();
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
