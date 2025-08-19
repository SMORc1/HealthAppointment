import java.util.*;

public class HealthAppointmentSystem {
    private static final Scanner sc = new Scanner(System.in);
    private static List<Patients> patients = new ArrayList<>();
    private static List<Doctors> doctors = new ArrayList<>();
    private static List<Appointment> appointments = new ArrayList<>();
    private static Patients loggedInPatient = null;

    // Expose read-only login state
    public static boolean isLoggedIn() {
        return loggedInPatient != null;
    }

    public static Patients getLoggedInPatient() {
        return loggedInPatient;
    }

    // Initialize doctors
    public static void initializeDoctors() {
        doctors.add(new Doctors("Josep", "Cardiologist", "Mon-Wed 9AM-12PM"));
        doctors.add(new Doctors("Janmark", "Dermatologist", "Tue-Thu 1PM-4PM"));
        doctors.add(new Doctors("Basta Doctor", "Pediatrician", "Fri 10AM-2PM"));
    }

    // Register new patient
    public static void registerPatient() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();
        System.out.print("Enter full name: ");
        String name = sc.nextLine();

        int age;
        while (true) {
            try {
                System.out.print("Enter age: ");
                age = Integer.parseInt(sc.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid age. Please enter a number.");
            }
        }

        System.out.print("Enter contact: ");
        String contact = sc.nextLine();

        patients.add(new Patients(username, password, name, age, contact));
        System.out.println("Registration successful!");
    }

    // Login
    public static void login() {
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        for (Patients p : patients) {
            if (p.getUsername().equals(username) && p.getPassword().equals(password)) {
                loggedInPatient = p;
                System.out.println("Welcome, " + p.getName() + "!");
                return;
            }
        }
        System.out.println("Invalid credentials.");
    }

    // Logout
    public static void logout() {
        loggedInPatient = null;
        System.out.println("Logged out.");
    }

    // Show Main Menu
    public static void showMainMenu() {
        while (isLoggedIn()) {
            System.out.println("\n=== Main Menu ===");
            System.out.println("1. View Patients");
            System.out.println("2. View Doctors");
            System.out.println("3. Book Appointment");
            System.out.println("4. View My Appointments");
            System.out.println("5. Cancel Appointment");
            System.out.println("6. Reschedule Appointment");
            System.out.println("7. Logout");
            System.out.print("Choose: ");

            String input = sc.nextLine();
            int choice;
            try {
                choice = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Enter a number.");
                continue;
            }

            switch (choice) {
                case 1 -> viewPatients();
                case 2 -> viewDoctors();
                case 3 -> bookAppointment();
                case 4 -> viewAppointments();
                case 5 -> cancelAppointment();
                case 6 -> rescheduleAppointment();
                case 7 -> logout();
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    private static void viewPatients() {
        System.out.println("\n=== Patient List ===");
        for (Patients p : patients) {
            System.out.println(p);
        }
    }

    private static void viewDoctors() {
        System.out.println("\n=== Doctors ===");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". " + doctors.get(i));
        }
    }

    private static void bookAppointment() {
        viewDoctors();
        System.out.print("Choose doctor number: ");
        int docIndex;
        try {
            docIndex = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (docIndex < 0 || docIndex >= doctors.size()) {
            System.out.println("Invalid doctor.");
            return;
        }

        System.out.print("Enter date & time (e.g., 2025-08-20 10:00AM): ");
        String dateTime = sc.nextLine();

        appointments.add(new Appointment(loggedInPatient, doctors.get(docIndex), dateTime));
        System.out.println("Appointment booked!");
    }

    private static void viewAppointments() {
        System.out.println("\n=== My Appointments ===");
        boolean found = false;
        for (Appointment a : appointments) {
            if (a.getPatient() == loggedInPatient) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) System.out.println("No appointments found.");
    }

    private static void cancelAppointment() {
        List<Appointment> myAppointments = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient() == loggedInPatient) myAppointments.add(a);
        }

        if (myAppointments.isEmpty()) {
            System.out.println("No appointments to cancel.");
            return;
        }

        for (int i = 0; i < myAppointments.size(); i++) {
            System.out.println((i + 1) + ". " + myAppointments.get(i));
        }

        System.out.print("Choose appointment to cancel: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (index < 0 || index >= myAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        appointments.remove(myAppointments.get(index));
        System.out.println("Appointment canceled.");
    }

    private static void rescheduleAppointment() {
        List<Appointment> myAppointments = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient() == loggedInPatient) myAppointments.add(a);
        }

        if (myAppointments.isEmpty()) {
            System.out.println("No appointments to reschedule.");
            return;
        }

        for (int i = 0; i < myAppointments.size(); i++) {
            System.out.println((i + 1) + ". " + myAppointments.get(i));
        }

        System.out.print("Choose appointment to reschedule: ");
        int index;
        try {
            index = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid input.");
            return;
        }

        if (index < 0 || index >= myAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Enter new date & time (e.g., 2025-08-25 2:00PM): ");
        String newDateTime = sc.nextLine();

        myAppointments.get(index).setDateTime(newDateTime);
        System.out.println("Appointment rescheduled successfully!");
    }

    // Health Appointment Entry
    public static void main(String[] args) {
        initializeDoctors();

        while (true) {
            if (!isLoggedIn()) {
                System.out.println("\n=== Welcome to Health Appointment System ===");
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.print("Choose: ");

                String input = sc.nextLine();
                int choice;
                try {
                    choice = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter a number.");
                    continue;
                }

                switch (choice) {
                    case 1 -> registerPatient();
                    case 2 -> {
                        login();
                        if (isLoggedIn()) {
                            showMainMenu();
                        }
                    }
                    case 3 -> {
                        System.out.println("Exiting...");
                        sc.close();
                        return;
                    }
                    default -> System.out.println("Invalid choice.");
                }
            }
        }
    }
}
