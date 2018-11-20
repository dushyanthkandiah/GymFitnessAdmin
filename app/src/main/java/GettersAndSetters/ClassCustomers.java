package GettersAndSetters;

public class ClassCustomers extends CommonAttributes {
    private int trainId;
    private String email, password;
    private Double height, weight;

    public ClassCustomers(int cusId, int schdId, String name, String address, String phone, String gender, String email, String password, String status, String dob, Double height, Double weight, byte[] photo) {
        super(cusId, name, dob, address, gender, phone, status, schdId, photo);

        this.email = email.replace("'", "''");
        this.password = password.replace("'", "''");
        this.height = height;
        this.weight = weight;
    }


    public ClassCustomers() {
        super();
    }

    public int getTrainId() {
        return trainId;
    }

    public void setTrainId(int trainId) {
        this.trainId = trainId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email.replace("'", "''");
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password.replace("'", "''");
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

}
