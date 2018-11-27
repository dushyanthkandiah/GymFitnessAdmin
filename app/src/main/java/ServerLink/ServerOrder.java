package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassBill;
import GettersAndSetters.ClassItemCart;

public class ServerOrder {

    private Database db;
    private ArrayList<ClassItemCart> list;
    private ClassItemCart classItemCart;

    public ServerOrder() {
        list = new ArrayList<>();
        classItemCart = new ClassItemCart();
    }


    public String getAllRecords() {
        db = new Database();
        String flag;

        String sql = "select `order`.*, supplements_and_products.name from `order` inner join supplements_and_products on supplements_and_products.`sup&prd_id` = `order`.`sup&prd_id` " +
                "where `order`.bill_id = " + classItemCart.getBillId();

//        System.out.println(sql + " ******");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";
//                    System.out.println(rs.getString("name") + "******");
                    list.add(new ClassItemCart(
                            rs.getInt("sup&prd_id"),
                            rs.getDouble("qty"),
                            rs.getDouble("sub_total"),
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




    public ArrayList<ClassItemCart> getList() {
        return list;
    }

    public ClassItemCart getClassItemCart() {
        return classItemCart;
    }
}
