package GettersAndSetters;


public class ClassSupplement {
    private int id;
    private String name, type;
    private double stock, price;

    public ClassSupplement() {
    }

    public ClassSupplement(int id, String name, String type, double stock, double price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.stock = stock;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getStock() {
        return stock;
    }

    public void setStock(double stock) {
        this.stock = stock;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
