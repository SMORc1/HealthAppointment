public class Doctors {
    private String name;
    private String specialization;
    private String schedule;

    public Doctors(String name, String specialization, String schedule) {
        this.name = name;
        this.specialization = specialization;
        this.schedule = schedule;
    }

    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getSchedule() { return schedule; }

    @Override
    public String toString() {
        return name + " - " + specialization + " (" + schedule + ")";
    }
}
