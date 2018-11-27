package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassNutritions;

public class ServerNutrition {

    private Database db;
    private ClassNutritions classNutritions;
    private ArrayList<ClassNutritions> list;

    public ServerNutrition() {
        classNutritions = new ClassNutritions();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        db = new Database();
        String flag;

        String sql = "select * from nutrition where status = 'active' and CONCAT(nut_id, food, type) like '%" + searchInput + "%' and shd_id = " + classNutritions.getSchdId() + " limit " + (limit * 5) + ", 5";

        if (classNutritions.getSchdId() == 0)
            sql = "select * from nutrition where status = 'active' and CONCAT(nut_id, food, type) like '%" + searchInput + "%' limit " + (limit * 5) + ", 5";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassNutritions(
                            rs.getInt("nut_id"),
                            rs.getInt("shd_id"),
                            rs.getString("food"),
                            rs.getString("type"),
                            rs.getString("status"),
                            rs.getBytes("picture")
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

            st = db.executeUpdate("INSERT INTO nutrition " +
                    "(food, " +
                    "type, " +
                    "picture, " +
                    "shd_id) " +
                    "VALUES(?, ?, ?, ?)");

            st.setString(1, classNutritions.getFood());
            st.setString(2, classNutritions.getType());
            st.setBytes(3, classNutritions.getPicture());
            st.setInt(4, classNutritions.getSchdId());
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

        PreparedStatement st = db.executeUpdate("UPDATE nutrition SET " +
                "food = ?, " +
                "type = ?, " +
                "picture = ?, " +
                "shd_id = ? " +
                "WHERE nut_id = ?");
        try {

            st.setString(1, classNutritions.getFood());
            st.setString(2, classNutritions.getType());
            st.setBytes(3, classNutritions.getPicture());
            st.setInt(4, classNutritions.getSchdId());
            st.setInt(5, classNutritions.getId());

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

        PreparedStatement st = db.executeUpdate("UPDATE nutrition SET " +
                "status = 'deactive' " +
                "WHERE nut_id = ?");
        try {

            st.setString(1, String.valueOf(classNutritions.getId()));

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ClassNutritions getClassNutritions() {
        return classNutritions;
    }

    public ArrayList<ClassNutritions> getList() {
        return list;
    }

}
