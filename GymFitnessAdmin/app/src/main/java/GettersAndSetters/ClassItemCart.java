package GettersAndSetters;


public class ClassItemCart {

    private int supId, billId;
    private double quantity, subTotal;
    private String itemName;

    public ClassItemCart(int supId, double quantity, double subTotal, String itemName) {

        this.supId = supId;
        this.quantity = quantity;
        this.subTotal = subTotal;
        this.itemName = itemName;
    }

    public ClassItemCart() {
    }


    public int getSupId() {
        return supId;
    }

    public void setSupId(int supId) {
        this.supId = supId;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }
}
