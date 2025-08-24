import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Doctors {
    private String name;
    private String specialization;
    private String scheduleDays;
    private LocalTime startTime;
    private LocalTime endTime;

    public Doctors(String name, String specialization, String scheduleDays, LocalTime startTime, LocalTime endTime) {
        this.name = name;
        this.specialization = specialization;
        this.scheduleDays = scheduleDays;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getName() { return name; }
    public String getSpecialization() { return specialization; }
    public String getScheduleDays() { return scheduleDays; }
    public LocalTime getStartTime() { return startTime; }
    public LocalTime getEndTime() { return endTime; }

    //Helper method which formats the time for display later
    private String getFormattedTime(LocalTime time) {
        return time.format(DateTimeFormatter.ofPattern("ha"));
    }

    @Override
    public String toString() {
        return name + " - " + specialization + " (" + scheduleDays + " " + getFormattedTime(startTime) + "-" + getFormattedTime(endTime) + ")";
    }

}
