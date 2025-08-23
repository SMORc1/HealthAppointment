/**
 * Things to add:
 * input validation (Done)
 * fix the appointment system (On going)
 *  - Make it so that you can't book from the past (Done)
 *  - Make it so that you can't set an appointment on two or more doctors at the same time and date (Done)
 *  - Make it so that you can't set an appointment on the same doctor twice (Done)
 *  - Provide a cap when setting an appointment ahead of time (e.g. 3 Months to 6 Months) (Done)
 *  - Registration of the same account details would invalidate the account registration (Done)
 *  - Invalidate account registration when inputting a registered phone number to another account (Done)
 *  - When registering make sure the patient enters his full name (Done)
 *  - Add a cap when entering the patient's age in the registration (e.g. 0-100) & invalidate any negatives (Done)
 *  - Invalidate book appointment when inputting a time that is out of the doctor's availability (Pending)
 *  - Invalidate reschedule appointment when changing the date from the past (Pending)
 *  - Invalidate a booking if a patient tries to schedule an appointment at a time that has already been booked with the same doctor, even if it’s through a different account (Pending)
 */


import java.util.Scanner;
import java.util.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = sc.nextLine();

            if (username.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again");
                continue;
            } else if (!username.matches("[A-Za-z0-9]+")) {
                System.out.println("Invalid username. Please try again");
                continue;
            }

            // Prevent duplicate usernames
            boolean exists = false;
            for (Patients p : patients) {
                if (p.getUsername().equalsIgnoreCase(username)) {
                    exists = true;
                    break;
                }
            }
            if (exists) {
                System.out.println("This username is already taken. Please try again.");
                continue;
            }

            break;
        }

        String password;
        while (true) {
            System.out.print("Password: ");
            password = sc.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again");
            } else {
                break;
            }
        }

        String name;
        while (true) {
            System.out.print("Enter full name: ");
            name = sc.nextLine();

            if (name.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again");
            }
            // Require at least first and last name
            else if (!name.matches("^[A-Za-z]+([ .'-][A-Za-z]+)+$")) {
                System.out.println("Invalid full name. Please enter at least first and last name.");
            } else {
                break;
            }
        }

        int age;
        while (true) {
            System.out.print("Enter age: ");
            if (sc.hasNextInt()) {
                age = sc.nextInt();
                sc.nextLine();

                if (age >= 0 && age <= 130) {
                    break;
                } else {
                    System.out.println("Invalid age. Please try again");
                }
            } else {
                System.out.println("Invalid input. Please enter a whole number.");
                sc.next();
            }
        }

        String contact;
        while (true) {
            System.out.print("Enter contact number: ");
            contact = sc.nextLine();

            if (!contact.matches("\\d{11}")) {
                System.out.println("Invalid contact number. Please enter exactly 11 digits.");
                continue;
            }

            // Prevent duplicate contact numbers
            boolean contactExists = false;
            for (Patients p : patients) {
                if (p.getContact().equals(contact)) {
                    contactExists = true;
                    break;
                }
            }
            if (contactExists) {
                System.out.println("This contact number is already registered. Please use another.");
                continue;
            }

            break;
        }

        patients.add(new Patients(username, password, name, age, contact));
        System.out.println("Registration successful!");
    }

    // Login
    public static void login() {
        String username;
        while (true) {
            System.out.print("Enter username: ");
            username = sc.nextLine();
            if (username.isEmpty()) {
                System.out.println("Name cannot be empty. Please try again");
            } else if (!username.matches("[A-Za-z0-9]+")) {
                System.out.println("Invalid username. Please try again");
            } else {
                break;
            }
        }

        String password;
        while (true) {
            System.out.print("Password: ");
            password = sc.nextLine();
            if (password.isEmpty()) {
                System.out.println("Password cannot be empty. Please try again");
            } else {
                break;
            }
        }

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
        while (true) {
            System.out.print("Are you sure you want to log out? (Y/N): ");
            String confirm = sc.nextLine().trim();

            if (confirm.equalsIgnoreCase("Y")) {
                loggedInPatient = null;
                System.out.println("Logged out successfully.");
                break;
            } else if (confirm.equalsIgnoreCase("N")) {
                System.out.println("Logout canceled.");
                break;
            } else {
                System.out.println("Invalid input. Please enter Y or N.");
            }
        }
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

    // View patients
    public static void viewPatients() {
        System.out.println("\n=== Patient List ===");
        for (Patients p : patients) {
            System.out.println(p);
        }
    }

    // View doctors
    public static void viewDoctors() {
        System.out.println("\n=== Doctor List ===");
        for (Doctors d : doctors) {
            System.out.println(d);
        }
    }

    // Book appointment
    public static void bookAppointment() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            return;
        }

        System.out.println("\nChoose a doctor:");
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println((i + 1) + ". " + doctors.get(i));
        }

        int docChoice;
        try {
            docChoice = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        if (docChoice < 0 || docChoice >= doctors.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Enter appointment date/time (yyyy-MM-dd HH:mm): ");
        String dateTime = sc.nextLine();
        try {
            LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        appointments.add(new Appointment(loggedInPatient, doctors.get(docChoice), dateTime));
        System.out.println("Appointment booked successfully!");
    }

    // View appointments
    public static void viewAppointments() {
        System.out.println("\n=== My Appointments ===");
        for (Appointment a : appointments) {
            if (a.getPatient().equals(loggedInPatient)) {
                System.out.println(a);
            }
        }
    }

    // Cancel appointment
    public static void cancelAppointment() {
        List<Appointment> myAppointments = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient().equals(loggedInPatient)) {
                myAppointments.add(a);
            }
        }

        if (myAppointments.isEmpty()) {
            System.out.println("No appointments to cancel.");
            return;
        }

        System.out.println("\nChoose an appointment to cancel:");
        for (int i = 0; i < myAppointments.size(); i++) {
            System.out.println((i + 1) + ". " + myAppointments.get(i));
        }

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        if (choice < 0 || choice >= myAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        appointments.remove(myAppointments.get(choice));
        System.out.println("Appointment canceled.");
    }

    // Reschedule appointment
    public static void rescheduleAppointment() {
        List<Appointment> myAppointments = new ArrayList<>();
        for (Appointment a : appointments) {
            if (a.getPatient().equals(loggedInPatient)) {
                myAppointments.add(a);
            }
        }

        if (myAppointments.isEmpty()) {
            System.out.println("No appointments to reschedule.");
            return;
        }

        System.out.println("\nChoose an appointment to reschedule:");
        for (int i = 0; i < myAppointments.size(); i++) {
            System.out.println((i + 1) + ". " + myAppointments.get(i));
        }

        int choice;
        try {
            choice = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        if (choice < 0 || choice >= myAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        System.out.print("Enter new date/time (yyyy-MM-dd HH:mm): ");
        String newDateTime = sc.nextLine();
        try {
            LocalDateTime.parse(newDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        myAppointments.get(choice).setDateTime(newDateTime);
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
