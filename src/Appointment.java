public class Appointment {
    private final Patients patient;
    private final Doctors doctor;
    private String dateTime;

    public Appointment(Patients patient, Doctors doctor, String dateTime) {
        this.patient = patient;
        this.doctor = doctor;
        this.dateTime = dateTime;
    }

    public Patients getPatient() { return patient; }
    public Doctors getDoctor() { return doctor; }
    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }

    @Override
    public String toString() {
        return "Appointment: " + patient.getName() +
                " with Dr. " + doctor.getName() +
                " on " + dateTime;
    }
}
