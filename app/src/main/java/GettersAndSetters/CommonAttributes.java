package GettersAndSetters;

public abstract class CommonAttributes {
    private String name, dob, address, gender, phone, status;
    private int id, schdId;
    private byte[] picture;

    public CommonAttributes(int id, String name, String dob, String address, String gender, String phone, String status, int schdId, byte[] picture) {
        this.id = id;
        this.name = name.replace("'", "''");
        this.dob = dob.replace("'", "''");
        this.address = address.replace("'", "''");
        this.gender = gender.replace("'", "''");
        this.phone = phone.replace("'", "''");
        this.status = status.replace("'", "''");
        this.schdId = schdId;
        this.picture = picture;
    }

    public CommonAttributes() {
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
        this.name = name.replace("'", "''");
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob.replace("'", "''");
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address.replace("'", "''");
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender.replace("'", "''");
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone.replace("'", "''");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.replace("'", "''");
    }

    public int getSchdId() {
        return schdId;
    }

    public void setSchdId(int schdId) {
        this.schdId = schdId;
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }
}
