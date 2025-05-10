package com.hospital.main;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;
import com.hospital.util.DatabaseUtil;

import java.util.List;
import java.util.Scanner;

/**
 * Main application class that demonstrates CRUD operations on Patient entity.
 */
public class HospitalApp {
    private static final PatientDAO patientDAO = new PatientDAO();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean exit = false;

        System.out.println("Welcome to Hospital Information System");

        while (!exit) {
            System.out.println("\n--- Patient Management ---");
            System.out.println("1. Add new patient");
            System.out.println("2. View patient details");
            System.out.println("3. View all patients");
            System.out.println("4. Update patient information");
            System.out.println("5. Delete patient");
            System.out.println("6. Search patients by name");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    addPatient();
                    break;
                case 2:
                    viewPatient();
                    break;
                case 3:
                    viewAllPatients();
                    break;
                case 4:
                    updatePatient();
                    break;
                case 5:
                    deletePatient();
                    break;
                case 6:
                    searchPatientsByName();
                    break;
                case 0:
                    exit = true;
                    System.out.println("Exiting application...");
                    DatabaseUtil.closeConnection();
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private static void addPatient() {
        System.out.println("\n--- Add New Patient ---");

        System.out.print("First Name: ");
        String firstName = scanner.nextLine();

        System.out.print("Last Name: ");
        String lastName = scanner.nextLine();

        System.out.print("Address: ");
        String address = scanner.nextLine();

        System.out.print("Telephone: ");
        String telephone = scanner.nextLine();

        Patient patient = new Patient(firstName, lastName, address, telephone);
        int patientId = patientDAO.createPatient(patient);

        if (patientId > 0) {
            System.out.println("Patient added successfully with ID: " + patientId);
        } else {
            System.out.println("Failed to add patient.");
        }
    }

    private static void viewPatient() {
        System.out.println("\n--- View Patient Details ---");
        System.out.print("Enter Patient ID: ");
        int patientId = getIntInput();

        Patient patient = patientDAO.readPatient(patientId);

        if (patient != null) {
            displayPatient(patient);
        } else {
            System.out.println("Patient not found with ID: " + patientId);
        }
    }

    private static void viewAllPatients() {
        System.out.println("\n--- All Patients ---");
        List<Patient> patients = patientDAO.readAllPatients();

        if (patients.isEmpty()) {
            System.out.println("No patients found in the database.");
        } else {
            for (Patient patient : patients) {
                displayPatient(patient);
                System.out.println("-------------------------");
            }
            System.out.println("Total patients: " + patients.size());
        }
    }

    private static void updatePatient() {
        System.out.println("\n--- Update Patient Information ---");
        System.out.print("Enter Patient ID to update: ");
        int patientId = getIntInput();

        Patient patient = patientDAO.readPatient(patientId);

        if (patient != null) {
            System.out.println("Current details:");
            displayPatient(patient);
            System.out.println("\nEnter new details (leave blank to keep current value):");

            System.out.print("First Name [" + patient.getFirstName() + "]: ");
            String firstName = scanner.nextLine();
            if (!firstName.trim().isEmpty()) {
                patient.setFirstName(firstName);
            }

            System.out.print("Last Name [" + patient.getLastName() + "]: ");
            String lastName = scanner.nextLine();
            if (!lastName.trim().isEmpty()) {
                patient.setLastName(lastName);
            }

            System.out.print("Address [" + patient.getAddress() + "]: ");
            String address = scanner.nextLine();
            if (!address.trim().isEmpty()) {
                patient.setAddress(address);
            }

            System.out.print("Telephone [" + patient.getTelephone() + "]: ");
            String telephone = scanner.nextLine();
            if (!telephone.trim().isEmpty()) {
                patient.setTelephone(telephone);
            }

            boolean updated = patientDAO.updatePatient(patient);
            if (updated) {
                System.out.println("Patient information updated successfully.");
            } else {
                System.out.println("Failed to update patient information.");
            }
        } else {
            System.out.println("Patient not found with ID: " + patientId);
        }
    }

    private static void deletePatient() {
        System.out.println("\n--- Delete Patient ---");
        System.out.print("Enter Patient ID to delete: ");
        int patientId = getIntInput();

        Patient patient = patientDAO.readPatient(patientId);
        if (patient != null) {
            System.out.println("This will delete the following patient:");
            displayPatient(patient);

            System.out.print("Are you sure you want to delete this patient? (y/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                boolean deleted = patientDAO.deletePatient(patientId);
                if (deleted) {
                    System.out.println("Patient deleted successfully.");
                } else {
                    System.out.println("Failed to delete patient.");
                }
            } else {
                System.out.println("Delete operation cancelled.");
            }
        } else {
            System.out.println("Patient not found with ID: " + patientId);
        }
    }

    private static void searchPatientsByName() {
        System.out.println("\n--- Search Patients by Name ---");
        System.out.print("Enter search name: ");
        String name = scanner.nextLine();

        List<Patient> patients = patientDAO.searchPatientsByName(name);

        if (patients.isEmpty()) {
            System.out.println("No patients found matching: " + name);
        } else {
            System.out.println("Found " + patients.size() + " matching patients:");
            for (Patient patient : patients) {
                displayPatient(patient);
                System.out.println("-------------------------");
            }
        }
    }

    private static void displayPatient(Patient patient) {
        System.out.println("Patient ID: " + patient.getPatientId());
        System.out.println("Name: " + patient.getFirstName() + " " + patient.getLastName());
        System.out.println("Address: " + patient.getAddress());
        System.out.println("Telephone: " + patient.getTelephone());
    }

    private static int getIntInput() {
        try {
            String input = scanner.nextLine();
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            return -1; // Invalid input
        }
    }
}
