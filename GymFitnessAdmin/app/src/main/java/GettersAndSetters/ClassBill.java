package GettersAndSetters;


public class ClassBill {

    private int billId, cusId, status;
    private String billDate;
    private Double total;

    public ClassBill(int cusId, String billDate, Double total) {

        this.cusId = cusId;
        this.billDate = billDate;
        this.total = total;
    }

    public ClassBill() {

    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getCusId() {
        return cusId;
    }

    public void setCusId(int cusId) {
        this.cusId = cusId;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

}
