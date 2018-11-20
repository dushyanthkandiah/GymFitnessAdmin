package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassBill;
import GettersAndSetters.ClassItemCart;

public class ServerBilling {

    private Database db;
    private ArrayList<ClassItemCart> itemList;
    private ClassBill classBill;
    private int lastGeneratedBillId;

    public ServerBilling() {
        db = new Database();
        itemList = new ArrayList<>();
        classBill = new ClassBill();
    }


    public int Save() {
        int result = 0;
        PreparedStatement st;

        try {

            st = db.executeUpdate("INSERT INTO bill " +
                    "(cus_id, " +
                    "bill_date, " +
                    "total) " +
                    "VALUES(?, CURDATE(), ?)");

            st.setInt(1, classBill.getCusId());
            st.setString(2, String.valueOf(classBill.getTotal()));
//            System.out.println(st);

            result = st.executeUpdate();

            ResultSet rs = st.getGeneratedKeys();
            rs.next();
            lastGeneratedBillId = rs.getInt(1);

            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public int SaveOrder() {
        int result = 0;
        PreparedStatement st;

        try {

            String sql = "INSERT INTO `order` (bill_id, sup_id, qty, sub_total) VALUES";

            for (int i = 0; i < itemList.size(); i++) {
                sql += "(" + lastGeneratedBillId + ", " + itemList.get(i).getSupId() + ", " + itemList.get(i).getQuantity() + ", " + itemList.get(i).getSubTotal() + "),";
            }
            sql = sql.substring(0, sql.length() - 1);

            st = db.executeUpdate(sql);

            result = st.executeUpdate();

            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }


    public ArrayList<ClassItemCart> getItemList() {
        return itemList;
    }

    public void setItemList(ArrayList<ClassItemCart> itemList) {
        this.itemList = itemList;
    }

    public ClassBill getClassBill() {
        return classBill;
    }

    public int getLastGeneratedBillId() {
        return lastGeneratedBillId;
    }
}
