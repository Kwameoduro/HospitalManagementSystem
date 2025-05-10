package com.hospital.dao;

import com.hospital.model.Patient;
import com.hospital.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Patient Data Access Object.
 * Provides CRUD operations for Patient entities.
 */
public class PatientDAO {

    /**
     * Create a new patient record in the database
     * @param patient Patient object to be added
     * @return the generated patient ID if successful, -1 if failed
     */
    public int createPatient(Patient patient) {
        String sql = "INSERT INTO patient (first_name, last_name, address, telephone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Set parameters
            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setString(3, patient.getAddress());
            stmt.setString(4, patient.getTelephone());

            // Execute the insert
            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating patient failed, no rows affected.");
            }

            // Get the generated patient ID
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    patient.setPatientId(id); // Update the patient object with the new ID
                    return id;
                } else {
                    throw new SQLException("Creating patient failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating patient: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Read a patient record from the database by ID
     * @param patientId the ID of the patient to retrieve
     * @return the Patient object if found, null if not found
     */
    public Patient readPatient(int patientId) {
        String sql = "SELECT * FROM patient WHERE patient_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone")
                );
            } else {
                return null; // Patient not found
            }
        } catch (SQLException e) {
            System.out.println("Error reading patient: " + e.getMessage());
            return null;
        }
    }

    /**
     * Read all patients from the database
     * @return List of all Patient objects
     */
    public List<Patient> readAllPatients() {
        String sql = "SELECT * FROM patient";
        List<Patient> patients = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.out.println("Error reading all patients: " + e.getMessage());
        }

        return patients;
    }

    /**
     * Update a patient record in the database
     * @param patient Patient object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patient SET first_name = ?, last_name = ?, address = ?, telephone = ? WHERE patient_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, patient.getFirstName());
            stmt.setString(2, patient.getLastName());
            stmt.setString(3, patient.getAddress());
            stmt.setString(4, patient.getTelephone());
            stmt.setInt(5, patient.getPatientId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating patient: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a patient record from the database
     * @param patientId the ID of the patient to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deletePatient(int patientId) {
        String sql = "DELETE FROM patient WHERE patient_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, patientId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting patient: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search for patients by name (partial match)
     * @param name the name to search for
     * @return List of matching Patient objects
     */
    public List<Patient> searchPatientsByName(String name) {
        String sql = "SELECT * FROM patient WHERE first_name LIKE ? OR last_name LIKE ?";
        List<Patient> patients = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + name + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Patient patient = new Patient(
                        rs.getInt("patient_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone")
                );
                patients.add(patient);
            }
        } catch (SQLException e) {
            System.out.println("Error searching patients: " + e.getMessage());
        }

        return patients;
    }
}
