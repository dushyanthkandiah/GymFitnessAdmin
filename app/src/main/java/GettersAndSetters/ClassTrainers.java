package GettersAndSetters;

public class ClassTrainers extends CommonAttributes {

    private String workExperience;

    public ClassTrainers(int id, String name, String dob, String address, String gender, String phone, String workExperience, byte[] photo, String status, int schdId) {
        super(id, name, dob, address, gender, phone, status, schdId, photo);
        this.workExperience = workExperience.replace("'", "''");
    }

    public ClassTrainers() {
        super();
    }

    public String getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(String workExperience) {
        this.workExperience = workExperience.replace("'", "''");
    }
}
