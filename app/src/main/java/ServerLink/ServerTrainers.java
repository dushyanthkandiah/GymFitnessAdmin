package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassTrainers;

public class ServerTrainers {

    private Database db;
    private ClassTrainers classTrainers;
    private ArrayList<ClassTrainers> list;

    public ServerTrainers() {
        classTrainers = new ClassTrainers();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        db = new Database();
        String flag;

        String sql = "select * from trainers where status = 'active' and CONCAT(train_id, name, phone) like '%" + searchInput + "%' and shd_id = " + classTrainers.getSchdId() + " limit " + (limit * 10) + ", 10";

        if (classTrainers.getSchdId() == 0)
            sql = "select * from trainers where status = 'active' and  CONCAT(train_id, name, phone) like '%" + searchInput + "%' limit " + (limit * 10) + ", 10";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassTrainers(
                            rs.getInt("train_id"),
                            rs.getString("name"),
                            rs.getString("dob"),
                            rs.getString("address"),
                            rs.getString("gender"),
                            rs.getString("phone"),
                            rs.getString("experience"),
                            rs.getBytes("picture"),
                            rs.getString("status"),
                            rs.getInt("shd_id")
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

    public String getAllRecords() {
        db = new Database();
        String flag;

        String sql = "select train_id, name from trainers where status = 'active'";

//        System.out.println(sql + " ******");

        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);
            list = new ArrayList<>();
            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";

                    ClassTrainers classTrainers = new ClassTrainers();
                    classTrainers.setId(rs.getInt("train_id"));
                    classTrainers.setName(rs.getString("name"));

                    list.add(classTrainers);

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

            st = db.executeUpdate("INSERT INTO trainers " +
                    "(name, " +
                    "dob, " +
                    "address, " +
                    "gender, " +
                    "phone, " +
                    "experience, " +
                    "picture, " +
                    "shd_id) " +
                    "VALUES(?, ?, ?, ?, ?, ?, ?, ?)");

            st.setString(1, classTrainers.getName());
            st.setString(2, classTrainers.getDob());
            st.setString(3, classTrainers.getAddress());
            st.setString(4, classTrainers.getGender());
            st.setString(5, classTrainers.getPhone());
            st.setString(6, classTrainers.getWorkExperience());
            st.setBytes(7, classTrainers.getPicture());
            st.setInt(8, classTrainers.getSchdId());
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


        PreparedStatement st = db.executeUpdate("UPDATE trainers SET " +
                "name = ?, " +
                "dob = ?, " +
                "address = ?, " +
                "gender = ?, " +
                "phone = ?, " +
                "experience = ?, " +
                "picture = ?, " +
                "shd_id = ? " +
                "WHERE train_id = ?");
        try {

            st.setString(1, classTrainers.getName());
            st.setString(2, classTrainers.getDob());
            st.setString(3, classTrainers.getAddress());
            st.setString(4, classTrainers.getGender());
            st.setString(5, classTrainers.getPhone());
            st.setString(6, classTrainers.getWorkExperience());
            st.setBytes(7, classTrainers.getPicture());
            st.setInt(8, classTrainers.getSchdId());
            st.setInt(9, classTrainers.getId());

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


        PreparedStatement st = db.executeUpdate("UPDATE trainers SET " +
                "status = 'deactive' " +
                "WHERE train_id = ?");
        try {

            st.setString(1, String.valueOf(classTrainers.getId()));

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }


    public ClassTrainers getClassTrainers() {
        return classTrainers;
    }

    public ArrayList<ClassTrainers> getList() {
        return list;
    }
}
