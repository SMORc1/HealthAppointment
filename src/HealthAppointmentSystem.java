/*
 * Things to add:
 * input validation (Done)
 * fix the appointment system (All Done)
 *  - Make it so that you can't book from the past (Done)
 *  - Make it so that you can't set an appointment on two or more doctors at the same time and date (Done)
 *  - Make it so that you can't set an appointment on the same doctor twice (Done)
 *  - Provide a cap when setting an appointment ahead of time (e.g. 3 Months to 6 Months) (Done)
 *  - Registration of the same account details would invalidate the account registration (Done)
 *  - Invalidate account registration when inputting a registered phone number to another account (Done)
 *  - When registering make sure the patient enters his full name (Done)
 *  - Add a cap when entering the patient's age in the registration (e.g. 0-100) & invalidate any negatives (Done)
 *  - Invalidate reschedule appointment when changing the date from the past (Done)
 *  - Invalidate a booking if a patient tries to schedule an appointment at a time that has already been booked with the same doctor, even if it’s through a different account (Done)
 */

import java.util.Scanner;
import java.util.*;
import java.time.LocalDate;
import java.time.LocalTime;
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
        doctors.add(new Doctors("Irving", "Cardiologist", "Mon-Wed", LocalTime.of(9, 0), LocalTime.of(12, 0)));
        doctors.add(new Doctors("Joseph", "Dermatologist", "Tue-Thu", LocalTime.of(13, 0), LocalTime.of(16, 0)));
        doctors.add(new Doctors("James", "Pediatrician", "Fri", LocalTime.of(10, 0), LocalTime.of(14, 0)));
        doctors.add(new Doctors("Irish", "Ophthalmology", "Sat", LocalTime.of(9, 0), LocalTime.of(13, 0)));
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

    private static boolean DocAvailability(Doctors doctor, LocalDate date) {
        java.time.DayOfWeek dayOfWeek = date.getDayOfWeek();
        String scheduleDays = doctor.getScheduleDays(); // like "Mon-Wed" or "Fri"

        Map<String, java.time.DayOfWeek> dayMap = new HashMap<>();
        dayMap.put("Mon", java.time.DayOfWeek.MONDAY);
        dayMap.put("Tue", java.time.DayOfWeek.TUESDAY);
        dayMap.put("Wed", java.time.DayOfWeek.WEDNESDAY);
        dayMap.put("Thu", java.time.DayOfWeek.THURSDAY);
        dayMap.put("Fri", java.time.DayOfWeek.FRIDAY);
        dayMap.put("Sat", java.time.DayOfWeek.SATURDAY);
        dayMap.put("Sun", java.time.DayOfWeek.SUNDAY);

        if (scheduleDays.contains("-")) { // if the doctor has schedule like from Mon-Wed
            String[] dayRange = scheduleDays.split("-");
            java.time.DayOfWeek startDay = dayMap.get(dayRange[0]);
            java.time.DayOfWeek endDay = dayMap.get(dayRange[1]);
            return dayOfWeek.getValue() >= startDay.getValue() && dayOfWeek.getValue() <= endDay.getValue();
        } else { //otherwise, it's a single day schedule
            java.time.DayOfWeek scheduledDay = dayMap.get(scheduleDays);
            return dayOfWeek == scheduledDay;
        }
    }

    // Book appointment
    public static void bookAppointment() {
        if (doctors.isEmpty()) {
            System.out.println("No doctors available.");
            return;
        }
        Doctors selectedDoctor;
        while (true) {
            System.out.println("\nChoose a doctor:");
            for (int i = 0; i < doctors.size(); i++) {
                System.out.println((i + 1) + ". " + doctors.get(i));
            }

            int docChoice;
            try {
                System.out.print("Enter choice: ");
                docChoice = Integer.parseInt(sc.nextLine()) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (docChoice < 0 || docChoice >= doctors.size()) {
                System.out.println("Invalid choice. Please try again.");
                continue;
            }
            selectedDoctor = doctors.get(docChoice);
            break;
        }


        //Setting up validation for appointment
        LocalDate appointmentDate;
        String dateStr;
        while (true) {
            System.out.print("Enter appointment date (yyyy-MM-dd): ");
            dateStr = sc.nextLine();
            try {
                appointmentDate = LocalDate.parse(dateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                continue;
            }

            if (appointmentDate.isBefore(LocalDate.now())) {
                System.out.println("Invalid booking date. Selected date is in the past. Please try again.");
                continue;
            }

            if (!DocAvailability(selectedDoctor, appointmentDate)) {
                System.out.println("Invalid. Dr. " + selectedDoctor.getName() + " is not available on " + appointmentDate.getDayOfWeek() + "'s. Please choose a different date.");
                continue;
            }
            break;
        }

        System.out.println("\nAvailable slots for " + dateStr + ":");
        LocalTime slotTime = selectedDoctor.getStartTime();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<LocalTime> allPossibleSlots = new ArrayList<>();
        List<Boolean> isSlotBooked = new ArrayList<>();

        while (slotTime.isBefore(selectedDoctor.getEndTime())) {
            allPossibleSlots.add(slotTime);
            String formattedDateTime = dateStr + " " + slotTime.format(timeFormatter);
            boolean booked = appointments.stream().anyMatch(a -> a.getDoctor().equals(selectedDoctor) && a.getDateTime().equals(formattedDateTime));
            isSlotBooked.add(booked);
            slotTime = slotTime.plusMinutes(30);
        }

        // Display all slots with their status if it's AVAILABLE or BOOKED
        boolean hasAvailableSlots = false;
        for (int i = 0; i < allPossibleSlots.size(); i++) {
            System.out.print((i + 1) + ". " + allPossibleSlots.get(i).format(timeFormatter));
            if (isSlotBooked.get(i)) {
                System.out.println(" - Booked");
            } else {
                System.out.println(" - Available");
                hasAvailableSlots = true;
            }
        }

        if (!hasAvailableSlots) {
            System.out.println("No available slots for this day.");
            return;
        }

        LocalTime chosenTime;
        while (true) {
            System.out.print("Choose an available slot: ");
            int slotChoice;
            try {
                slotChoice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (slotChoice < 1 || slotChoice > allPossibleSlots.size()) {
                System.out.println("Invalid slot number. Please try again.");
                continue;
            }

            // This check if the CHOSEN slot is already booked by someone else
            if (isSlotBooked.get(slotChoice - 1)) {
                System.out.println("This slot is already booked. Please choose an available slot.");
                continue;
            }

            chosenTime = allPossibleSlots.get(slotChoice - 1);
            break;
        }

        String finalDateTime = dateStr + " " + chosenTime.format(timeFormatter);
        appointments.add(new Appointment(loggedInPatient, selectedDoctor, finalDateTime));
        System.out.println("Appointment booked successfully for " + finalDateTime + "!");
    }


    // View appointments
    public static void viewAppointments() {
        System.out.println("\n=== My Appointments ===");
        boolean found = false;
        for (Appointment a : appointments) {
            if (a.getPatient().equals(loggedInPatient)) {
                System.out.println(a);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No appointment booked for " + loggedInPatient + "!");
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
            System.out.println("Enter choice: ");
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
            System.out.println("Enter choice: ");
            choice = Integer.parseInt(sc.nextLine()) - 1;
        } catch (NumberFormatException e) {
            System.out.println("Invalid choice.");
            return;
        }

        if (choice < 0 || choice >= myAppointments.size()) {
            System.out.println("Invalid choice.");
            return;
        }

        Appointment appointToReschedule = myAppointments.get(choice);
        Doctors selectedDoctor = appointToReschedule.getDoctor();
        System.out.println("Rescheduling appointment with Dr. " + selectedDoctor.getName());

        LocalDate newAppointmentDate;
        String newDateStr;
        while (true) {// loop for Validation block
            System.out.print("Enter new appointment date (yyyy-MM-dd): ");
            newDateStr = sc.nextLine();
            try {
                newAppointmentDate = LocalDate.parse(newDateStr);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use yyyy-MM-dd.");
                continue;
            }

            if (newAppointmentDate.isBefore(LocalDate.now())) {
                System.out.println("Invalid booking date. Selected date is in the past. Please try again.");
                continue;
            }

            if (!DocAvailability(selectedDoctor, newAppointmentDate)) {
                System.out.println("Invalid. Dr. " + selectedDoctor.getName() + " is not available on " + newAppointmentDate.getDayOfWeek() + "s. Please choose a different date.");
                continue;
            }
            break;
        }


        System.out.println("\nAvailable slots for " + newDateStr + ":");
        LocalTime slotTime = selectedDoctor.getStartTime();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        List<LocalTime> allPossibleSlots = new ArrayList<>();
        List<Boolean> isSlotBooked = new ArrayList<>();

        while (slotTime.isBefore(selectedDoctor.getEndTime())) {
            allPossibleSlots.add(slotTime);
            String formattedDateTime = newDateStr + " " + slotTime.format(timeFormatter);
            boolean booked = appointments.stream().anyMatch(a -> a.getDoctor().equals(selectedDoctor) && a.getDateTime().equals(formattedDateTime));
            isSlotBooked.add(booked);
            slotTime = slotTime.plusMinutes(30);
        }

        boolean hasAvailableSlots = false;
        for (int i = 0; i < allPossibleSlots.size(); i++) {
            System.out.print((i + 1) + ". " + allPossibleSlots.get(i).format(timeFormatter));
            if (isSlotBooked.get(i)) {
                System.out.println(" - Booked");
            } else {
                System.out.println(" - Available");
                hasAvailableSlots = true;
            }
        }

        if (!hasAvailableSlots) {
            System.out.println("No available slots for this day.");
            return;
        }

        LocalTime newChosenTime;
        while (true) {
            System.out.print("Choose an available slot number: ");
            int slotChoice;
            try {
                slotChoice = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
                continue;
            }

            if (slotChoice < 1 || slotChoice > allPossibleSlots.size()) {
                System.out.println("Invalid slot number. Please try again.");
                continue;
            }

            if (isSlotBooked.get(slotChoice - 1)) {
                System.out.println("This slot is already booked. Please choose an available slot.");
                continue;
            }

            newChosenTime = allPossibleSlots.get(slotChoice - 1);
            break;
        }

        String finalDateTime = newDateStr + " " + newChosenTime.format(timeFormatter);
        appointToReschedule.setDateTime(finalDateTime);
        System.out.println("Appointment rescheduled successfully to " + finalDateTime + "!");
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
