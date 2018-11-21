package ServerLink;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassSupplement;

public class ServerSupplements {

    private Database db;
    private ClassSupplement classSupplement;
    private ArrayList<ClassSupplement> list;

    public ServerSupplements() {
        db = new Database();
        classSupplement = new ClassSupplement();
        list = new ArrayList<>();
    }

    public String getAllRecords(String searchInput, int limit) {
        String flag;

        String sql = "select * from supplements_and_products where CONCAT(`sup&prd_id`, name, type) like '%" + searchInput + "%' limit " + (limit * 20) + ", 20";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassSupplement(
                            rs.getInt("sup&prd_id"),
                            rs.getString("name"),
                            rs.getString("type"),
                            rs.getDouble("stock"),
                            rs.getDouble("price")
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

    public String getAllRecords(String searchInput) {
        String flag;

        String sql = "select * from supplements_and_products where CONCAT(`sup&prd_id`, name) like '%" + searchInput + "%' limit 0, 200";

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                if (!searchInput.equals("")) {

                    while (rs.next()) {
                        flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                        list.add(new ClassSupplement(
                                rs.getInt("sup&prd_id"),
                                rs.getString("name"),
                                rs.getString("type"),
                                rs.getDouble("stock"),
                                rs.getDouble("price")
                        ));
                    }
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

            st = db.executeUpdate("INSERT INTO supplements_and_products " +
                    "(name, " +
                    "stock, " +
                    "price, " +
                    "type) " +
                    "VALUES(?, ?, ?, ?)");

            st.setString(1, classSupplement.getName());
            st.setString(2, String.valueOf(classSupplement.getStock()));
            st.setString(3, String.valueOf(classSupplement.getPrice()));
            st.setString(4, classSupplement.getType());
//            System.out.println(st);

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int Update() {
        int result = 0;


        PreparedStatement st = db.executeUpdate("UPDATE supplements_and_products SET " +
                "name = ?, " +
                "stock = ?, " +
                "price = ?, " +
                "type = ? " +
                "WHERE `sup&prd_id` = ?");
        try {

            st.setString(1, classSupplement.getName());
            st.setDouble(2, classSupplement.getStock());
            st.setDouble(3, classSupplement.getPrice());
            st.setString(4, classSupplement.getType());
            st.setInt(5, classSupplement.getId());

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    public String isStockValid() {
        String flag;

        String sql = "select name from supplements_and_products where stock >= " + classSupplement.getStock() + " and `sup&prd_id` = " + classSupplement.getId();

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";


                    if (rs.next()) {
                        flag = "success";
                        classSupplement.setName(rs.getString("name"));
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


    public int ReduceStock() {
        int result = 0;

        PreparedStatement st = db.executeUpdate("UPDATE supplements_and_products SET " +
                "stock = stock - ? " +
                "WHERE `sup&prd_id` = ?");
        try {

            st.setDouble(1, classSupplement.getStock());
            st.setInt(2, classSupplement.getId());

            result = st.executeUpdate();
            System.out.println(st + " ***************");
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public ClassSupplement getClassSupplement() {
        return classSupplement;
    }

    public ArrayList<ClassSupplement> getList() {
        return list;
    }


}
