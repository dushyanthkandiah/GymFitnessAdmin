package GettersAndSetters;


public class ClassNutritions {

    private int id, schdId;
    private String food, type, status;
    private byte[] picture;

    public ClassNutritions(int id, int schdId, String food, String type, String status, byte[] picture) {
        this.id = id;
        this.food = food.replace("'", "''");
        this.type = type.replace("'", "''");
        this.status = status.replace("'", "''");
        this.picture = picture;
        this.schdId = schdId;
    }

    public ClassNutritions() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFood() {
        return food;
    }

    public void setFood(String food) {
        this.food = food.replace("'", "''");
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.replace("'", "''");
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status.replace("'", "''");
    }

    public byte[] getPicture() {
        return picture;
    }

    public void setPicture(byte[] picture) {
        this.picture = picture;
    }

    public int getSchdId() {
        return schdId;
    }

    public void setSchdId(int schdId) {
        this.schdId = schdId;
    }
}
