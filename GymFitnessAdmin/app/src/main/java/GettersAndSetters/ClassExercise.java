package GettersAndSetters;


public class ClassExercise {

    private int id, schdId, sets, times;
    private String excercise;

    public ClassExercise(int id, int schdId, int sets, int times, String excercise) {
        this.id = id;
        this.schdId = schdId;
        this.sets = sets;
        this.times = times;
        this.excercise = excercise;
    }

    public ClassExercise() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSchdId() {
        return schdId;
    }

    public void setSchdId(int schdId) {
        this.schdId = schdId;
    }

    public int getSets() {
        return sets;
    }

    public void setSets(int sets) {
        this.sets = sets;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getExcercise() {
        return excercise;
    }

    public void setExcercise(String excercise) {
        this.excercise = excercise;
    }
}
