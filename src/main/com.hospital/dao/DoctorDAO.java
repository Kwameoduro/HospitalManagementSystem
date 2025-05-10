package com.hospital.dao;

import com.hospital.model.Doctor;
import com.hospital.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Doctor Data Access Object.
 * Provides CRUD operations for Doctor entities.
 */
public class DoctorDAO {

    /**
     * Create a new doctor record in the database
     * @param doctor Doctor object to be added
     * @return the generated employee ID if successful, -1 if failed
     */
    public int createDoctor(Doctor doctor) {
        Connection conn = null;
        PreparedStatement stmtEmployee = null;
        PreparedStatement stmtDoctor = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, insert into employee table
            String sqlEmployee = "INSERT INTO employee (first_name, last_name, address, telephone) VALUES (?, ?, ?, ?)";
            stmtEmployee = conn.prepareStatement(sqlEmployee, Statement.RETURN_GENERATED_KEYS);

            stmtEmployee.setString(1, doctor.getFirstName());
            stmtEmployee.setString(2, doctor.getLastName());
            stmtEmployee.setString(3, doctor.getAddress());
            stmtEmployee.setString(4, doctor.getTelephone());

            int affectedRows = stmtEmployee.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating employee failed, no rows affected.");
            }

            // Get the generated employee ID
            try (ResultSet generatedKeys = stmtEmployee.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1);
                    doctor.setEmployeeId(employeeId);

                    // Now, insert into doctor table
                    String sqlDoctor = "INSERT INTO doctor (employee_id, speciality) VALUES (?, ?)";
                    stmtDoctor = conn.prepareStatement(sqlDoctor);

                    stmtDoctor.setInt(1, employeeId);
                    stmtDoctor.setString(2, doctor.getSpeciality());

                    stmtDoctor.executeUpdate();

                    conn.commit(); // Commit transaction
                    return employeeId;
                } else {
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating doctor: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction
                }
            } catch (SQLException ex) {
                System.out.println("Error rolling back transaction: " + ex.getMessage());
            }
            return -1;
        } finally {
            try {
                if (stmtDoctor != null) stmtDoctor.close();
                if (stmtEmployee != null) stmtEmployee.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Read a doctor record from the database by ID
     * @param employeeId the ID of the doctor to retrieve
     * @return the Doctor object if found, null if not found
     */
    public Doctor readDoctor(int employeeId) {
        String sql = "SELECT e.*, d.speciality FROM employee e " +
                "JOIN doctor d ON e.employee_id = d.employee_id " +
                "WHERE e.employee_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Doctor(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("speciality")
                );
            } else {
                return null; // Doctor not found
            }
        } catch (SQLException e) {
            System.out.println("Error reading doctor: " + e.getMessage());
            return null;
        }
    }

    /**
     * Read all doctors from the database
     * @return List of all Doctor objects
     */
    public List<Doctor> readAllDoctors() {
        String sql = "SELECT e.*, d.speciality FROM employee e " +
                "JOIN doctor d ON e.employee_id = d.employee_id";
        List<Doctor> doctors = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("speciality")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.out.println("Error reading all doctors: " + e.getMessage());
        }

        return doctors;
    }

    /**
     * Update a doctor record in the database
     * @param doctor Doctor object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateDoctor(Doctor doctor) {
        Connection conn = null;
        PreparedStatement stmtEmployee = null;
        PreparedStatement stmtDoctor = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update employee table
            String sqlEmployee = "UPDATE employee SET first_name = ?, last_name = ?, address = ?, telephone = ? WHERE employee_id = ?";
            stmtEmployee = conn.prepareStatement(sqlEmployee);

            stmtEmployee.setString(1, doctor.getFirstName());
            stmtEmployee.setString(2, doctor.getLastName());
            stmtEmployee.setString(3, doctor.getAddress());
            stmtEmployee.setString(4, doctor.getTelephone());
            stmtEmployee.setInt(5, doctor.getEmployeeId());

            stmtEmployee.executeUpdate();

            // Update doctor table
            String sqlDoctor = "UPDATE doctor SET speciality = ? WHERE employee_id = ?";
            stmtDoctor = conn.prepareStatement(sqlDoctor);

            stmtDoctor.setString(1, doctor.getSpeciality());
            stmtDoctor.setInt(2, doctor.getEmployeeId());

            stmtDoctor.executeUpdate();

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating doctor: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction
                }
            } catch (SQLException ex) {
                System.out.println("Error rolling back transaction: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (stmtDoctor != null) stmtDoctor.close();
                if (stmtEmployee != null) stmtEmployee.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Delete a doctor record from the database
     * @param employeeId the ID of the doctor to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteDoctor(int employeeId) {
        Connection conn = null;
        PreparedStatement stmtDoctor = null;
        PreparedStatement stmtEmployee = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First delete from doctor table (child)
            String sqlDoctor = "DELETE FROM doctor WHERE employee_id = ?";
            stmtDoctor = conn.prepareStatement(sqlDoctor);
            stmtDoctor.setInt(1, employeeId);
            stmtDoctor.executeUpdate();

            // Then delete from employee table (parent)
            String sqlEmployee = "DELETE FROM employee WHERE employee_id = ?";
            stmtEmployee = conn.prepareStatement(sqlEmployee);
            stmtEmployee.setInt(1, employeeId);
            int affectedRows = stmtEmployee.executeUpdate();

            conn.commit(); // Commit transaction
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting doctor: " + e.getMessage());
            try {
                if (conn != null) {
                    conn.rollback(); // Rollback transaction
                }
            } catch (SQLException ex) {
                System.out.println("Error rolling back transaction: " + ex.getMessage());
            }
            return false;
        } finally {
            try {
                if (stmtDoctor != null) stmtDoctor.close();
                if (stmtEmployee != null) stmtEmployee.close();
                if (conn != null) {
                    conn.setAutoCommit(true);
                    conn.close();
                }
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }
    }

    /**
     * Search for doctors by name (partial match) or speciality
     * @param query the name or speciality to search for
     * @return List of matching Doctor objects
     */
    public List<Doctor> searchDoctors(String query) {
        String sql = "SELECT e.*, d.speciality FROM employee e " +
                "JOIN doctor d ON e.employee_id = d.employee_id " +
                "WHERE e.first_name LIKE ? OR e.last_name LIKE ? OR d.speciality LIKE ?";
        List<Doctor> doctors = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Doctor doctor = new Doctor(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("speciality")
                );
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            System.out.println("Error searching doctors: " + e.getMessage());
        }

        return doctors;
    }
}
