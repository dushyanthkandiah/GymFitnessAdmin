package ServerLink;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import GettersAndSetters.ClassBill;
import GettersAndSetters.ClassItemCart;

public class ServerBilling {

    private Database db;
    private ArrayList<ClassBill> list;
    private ClassBill classBill;
    private int lastGeneratedBillId;

    public ServerBilling() {
        db = new Database();
        list = new ArrayList<>();
        classBill = new ClassBill();
    }

    public String getAllRecords(String fromDate, String toDate, int limit, String orderBy) {
        String flag;
        int maxLimit = 5;

        String sql = "select bill.*, customers.name from bill " +
                "inner join customers on bill.cus_id = customers.cus_id " +
                "where bill.`status` = " + classBill.getStatus() + " and (bill.bill_date BETWEEN '" + fromDate + "' AND '" + toDate + "') " +
                "order by bill.bill_id " + orderBy + " limit " + (limit * maxLimit) + ", " + maxLimit + "";

        if (classBill.getCusId() != 0)
            sql = "select bill.*, customers.name from bill " +
                    "inner join customers on bill.cus_id = customers.cus_id " +
                    "where bill.cus_id = " + classBill.getCusId() + " and bill.`status` = " + classBill.getStatus() + " and (bill.bill_date BETWEEN '" + fromDate + "' AND '" + toDate + "') " +
                    "order by bill.bill_id " + orderBy + " limit " + (limit * maxLimit) + ", " + maxLimit + "";


//        System.out.println(sql + " *********************");
        if (db.getConn() != null) {

            ResultSet rs = db.executeQuery(sql);

            try {
                flag = "nodata";
                list = new ArrayList<>();

                while (rs.next()) {
                    flag = "success";

                    list.add(new ClassBill(
                            rs.getInt("bill_id"),
                            rs.getInt("cus_id"),
                            rs.getString("bill_date"),
                            rs.getDouble("total"),
                            rs.getInt("status"),
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

    public int MarkStatusPending() {
        int result = 0;

        PreparedStatement st = db.executeUpdate("UPDATE bill SET " +
                "status = 1 " +
                "WHERE bill_id = ?");
        try {

            st.setInt(1, classBill.getBillId());

//            System.out.println(st + "**************");

            result = st.executeUpdate();
            st.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }



    public ArrayList<ClassBill> getList() {
        return list;
    }

    public ClassBill getClassBill() {
        return classBill;
    }

    public int getLastGeneratedBillId() {
        return lastGeneratedBillId;
    }
}
