package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassExercise;

public class ServerExercise {

    private Database db;
    private ClassExercise classExercise;
    private ArrayList<ClassExercise> list;

    public ServerExercise() {
        classExercise = new ClassExercise();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        db = new Database();
        String flag;

        String sql = "select * from exercise where CONCAT(exc_id, name) like '%" + searchInput + "%' and shd_id = " + classExercise.getSchdId() + " limit " + (limit * 15) + ", 15";

        if (classExercise.getSchdId() == 0)
            sql = "select * from exercise where CONCAT(exc_id, name) like '%" + searchInput + "%' limit " + (limit * 15) + ", 15";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassExercise(
                            rs.getInt("exc_id"),
                            rs.getInt("shd_id"),
                            rs.getInt("sets"),
                            rs.getInt("times"),
                            rs.getString("name")
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

            st = db.executeUpdate("INSERT INTO exercise " +
                    "(name, " +
                    "sets, " +
                    "times, " +
                    "shd_id) " +
                    "VALUES(?, ?, ?, ?)");

            st.setString(1, classExercise.getExcercise());
            st.setInt(2, classExercise.getSets());
            st.setInt(3, classExercise.getTimes());
            st.setInt(4, classExercise.getSchdId());
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


        PreparedStatement st = db.executeUpdate("UPDATE exercise SET " +
                "name = ?, " +
                "sets = ?, " +
                "times = ?, " +
                "shd_id = ? " +
                "WHERE exc_id = ?");
        try {

            st.setString(1, classExercise.getExcercise());
            st.setInt(2, classExercise.getSets());
            st.setInt(3, classExercise.getTimes());
            st.setInt(4, classExercise.getSchdId());
            st.setInt(5, classExercise.getId());

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }


    public ClassExercise getClassExercise() {
        return classExercise;
    }

    public ArrayList<ClassExercise> getList() {
        return list;
    }
}
