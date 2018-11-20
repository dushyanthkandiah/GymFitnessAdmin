package GettersAndSetters;


public class ClassSchedule {

    private int schdId, timePeriod;
    private String type;

    public ClassSchedule(int schdId, String type, int timePeriod) {
        this.schdId = schdId;
        this.timePeriod = timePeriod;
        this.type = type.replace("'", "''");
    }

    public ClassSchedule() {
    }

    public int getSchdId() {
        return schdId;
    }

    public void setSchdId(int packId) {
        this.schdId = packId;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public void setTimePeriod(int timePeriod) {
        this.timePeriod = timePeriod;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type.replace("'", "''");
    }

}
