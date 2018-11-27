package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassSchedule;

public class ServerSchedule {

    private Database db;
    private ClassSchedule classSchedule;
    private ArrayList<ClassSchedule> list;

    public ServerSchedule() {
        classSchedule = new ClassSchedule();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput) {
        db = new Database();
        String flag;

        String sql = "select * from schedule where CONCAT(shd_id, type) like '%" + searchInput + "%'";

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";

                    list.add(new ClassSchedule(
                            rs.getInt("shd_id"),
                            rs.getString("type"),
                            rs.getInt("time_period")
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

    public String Search() {
        db = new Database();
        String flag;

        String sql = "select * from schedule where shd_id = " + classSchedule.getSchdId();

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";

                if (rs.next()) {
                    flag = "success";
                    classSchedule.setType(rs.getString("type"));
                    classSchedule.setTimePeriod(rs.getInt("time_period"));
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

            st = db.executeUpdate("INSERT INTO schedule " +
                    "(type, " +
                    "time_period) " +
                    "VALUES(?, ?)");

            st.setString(1, classSchedule.getType());
            st.setInt(2, classSchedule.getTimePeriod());
//            System.out.println(st);

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int Update() {
        db = new Database();
        int result = 0;

        PreparedStatement st = db.executeUpdate("UPDATE schedule SET " +
                "type = ?, " +
                "time_period = ? " +
                "WHERE shd_id = ?");
        try {

            st.setString(1, classSchedule.getType());
            st.setInt(2, classSchedule.getTimePeriod());
            st.setInt(3, classSchedule.getSchdId());

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    public int Delete() {
        db = new Database();
        int result = 0;

        PreparedStatement st = db.executeUpdate("Delete from schedule WHERE shd_id = ?");
        try {

            st.setInt(1, classSchedule.getSchdId());

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ClassSchedule getClassSchedule() {
        return classSchedule;
    }

    public ArrayList<ClassSchedule> getList() {
        return list;
    }
}
