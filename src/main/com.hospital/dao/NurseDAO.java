package com.hospital.dao;

import com.hospital.model.Nurse;
import com.hospital.util.DatabaseUtil;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Nurse Data Access Object.
 * Provides CRUD operations for Nurse entities.
 */
public class NurseDAO {

    /**
     * Create a new nurse record in the database
     *
     * @param nurse Nurse object to be added
     * @return the generated employee ID if successful, -1 if failed
     */
    public int createNurse(Nurse nurse) {
        Connection conn = null;
        PreparedStatement stmtEmployee = null;
        PreparedStatement stmtNurse = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First, insert into employee table
            String sqlEmployee = "INSERT INTO employee (first_name, last_name, address, telephone) VALUES (?, ?, ?, ?)";
            stmtEmployee = conn.prepareStatement(sqlEmployee, Statement.RETURN_GENERATED_KEYS);

            stmtEmployee.setString(1, nurse.getFirstName());
            stmtEmployee.setString(2, nurse.getLastName());
            stmtEmployee.setString(3, nurse.getAddress());
            stmtEmployee.setString(4, nurse.getTelephone());

            int affectedRows = stmtEmployee.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating employee failed, no rows affected.");
            }

            // Get the generated employee ID
            try (ResultSet generatedKeys = stmtEmployee.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int employeeId = generatedKeys.getInt(1);
                    nurse.setEmployeeId(employeeId);

                    // Now, insert into nurse table
                    String sqlNurse = "INSERT INTO nurse (employee_id, rotation, salary, department_id) VALUES (?, ?, ?, ?)";
                    stmtNurse = conn.prepareStatement(sqlNurse);

                    stmtNurse.setInt(1, employeeId);
                    stmtNurse.setString(2, nurse.getRotation());
                    stmtNurse.setBigDecimal(3, nurse.getSalary());
                    stmtNurse.setInt(4, nurse.getDepartmentId());

                    stmtNurse.executeUpdate();

                    conn.commit(); // Commit transaction
                    return employeeId;
                } else {
                    throw new SQLException("Creating employee failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating nurse: " + e.getMessage());
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
                if (stmtNurse != null) stmtNurse.close();
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
     * Read a nurse record from the database by ID
     *
     * @param employeeId the ID of the nurse to retrieve
     * @return the Nurse object if found, null if not found
     */
    public Nurse readNurse(int employeeId) {
        String sql = "SELECT e.*, n.rotation, n.salary, n.department_id FROM employee e " +
                "JOIN nurse n ON e.employee_id = n.employee_id " +
                "WHERE e.employee_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, employeeId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Nurse(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("rotation"),
                        rs.getBigDecimal("salary"),
                        rs.getInt("department_id")
                );
            } else {
                return null; // Nurse not found
            }
        } catch (SQLException e) {
            System.out.println("Error reading nurse: " + e.getMessage());
            return null;
        }
    }

    /**
     * Read all nurses from the database
     *
     * @return List of all Nurse objects
     */
    public List<Nurse> readAllNurses() {
        String sql = "SELECT e.*, n.rotation, n.salary, n.department_id FROM employee e " +
                "JOIN nurse n ON e.employee_id = n.employee_id";
        List<Nurse> nurses = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Nurse nurse = new Nurse(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("rotation"),
                        rs.getBigDecimal("salary"),
                        rs.getInt("department_id")
                );
                nurses.add(nurse);
            }
        } catch (SQLException e) {
            System.out.println("Error reading all nurses: " + e.getMessage());
        }

        return nurses;
    }

    /**
     * Update a nurse record in the database
     *
     * @param nurse Nurse object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateNurse(Nurse nurse) {
        Connection conn = null;
        PreparedStatement stmtEmployee = null;
        PreparedStatement stmtNurse = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // Update employee table
            String sqlEmployee = "UPDATE employee SET first_name = ?, last_name = ?, address = ?, telephone = ? WHERE employee_id = ?";
            stmtEmployee = conn.prepareStatement(sqlEmployee);

            stmtEmployee.setString(1, nurse.getFirstName());
            stmtEmployee.setString(2, nurse.getLastName());
            stmtEmployee.setString(3, nurse.getAddress());
            stmtEmployee.setString(4, nurse.getTelephone());
            stmtEmployee.setInt(5, nurse.getEmployeeId());

            stmtEmployee.executeUpdate();

            // Update nurse table
            String sqlNurse = "UPDATE nurse SET rotation = ?, salary = ?, department_id = ? WHERE employee_id = ?";
            stmtNurse = conn.prepareStatement(sqlNurse);

            stmtNurse.setString(1, nurse.getRotation());
            stmtNurse.setBigDecimal(2, nurse.getSalary());
            stmtNurse.setInt(3, nurse.getDepartmentId());
            stmtNurse.setInt(4, nurse.getEmployeeId());

            stmtNurse.executeUpdate();

            conn.commit(); // Commit transaction
            return true;
        } catch (SQLException e) {
            System.out.println("Error updating nurse: " + e.getMessage());
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
                if (stmtNurse != null) stmtNurse.close();
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
     * Delete a nurse record from the database
     *
     * @param employeeId the ID of the nurse to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteNurse(int employeeId) {
        Connection conn = null;
        PreparedStatement stmtNurse = null;
        PreparedStatement stmtEmployee = null;

        try {
            conn = DatabaseUtil.getConnection();
            conn.setAutoCommit(false); // Start transaction

            // First delete from nurse table (child)
            String sqlNurse = "DELETE FROM nurse WHERE employee_id = ?";
            stmtNurse = conn.prepareStatement(sqlNurse);
            stmtNurse.setInt(1, employeeId);
            stmtNurse.executeUpdate();

            // Then delete from employee table (parent)
            String sqlEmployee = "DELETE FROM employee WHERE employee_id = ?";
            stmtEmployee = conn.prepareStatement(sqlEmployee);
            stmtEmployee.setInt(1, employeeId);
            int affectedRows = stmtEmployee.executeUpdate();

            conn.commit(); // Commit transaction
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting nurse: " + e.getMessage());
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
                if (stmtNurse != null) stmtNurse.close();
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
     * Search for nurses by name (partial match) or rotation
     *
     * @param query the name or rotation to search for
     * @return List of matching Nurse objects
     */
    public List<Nurse> searchNurses(String query) {
        String sql = "SELECT e.*, n.rotation, n.salary, n.department_id FROM employee e " +
                "JOIN nurse n ON e.employee_id = n.employee_id " +
                "WHERE e.first_name LIKE ? OR e.last_name LIKE ? OR n.rotation LIKE ?";
        List<Nurse> nurses = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);
            stmt.setString(3, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Nurse nurse = new Nurse(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("rotation"),
                        rs.getBigDecimal("salary"),
                        rs.getInt("department_id")
                );
                nurses.add(nurse);
            }
        } catch (SQLException e) {
            System.out.println("Error searching nurses: " + e.getMessage());
        }

        return nurses;
    }

    /**
     * Get all nurses from a specific department
     *
     * @param departmentId the ID of the department
     * @return List of Nurse objects in the department
     */
    public List<Nurse> getNursesByDepartment(int departmentId) {
        String sql = "SELECT e.*, n.rotation, n.salary, n.department_id FROM employee e " +
                "JOIN nurse n ON e.employee_id = n.employee_id " +
                "WHERE n.department_id = ?";
        List<Nurse> nurses = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Nurse nurse = new Nurse(
                        rs.getInt("employee_id"),
                        rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getString("address"),
                        rs.getString("telephone"),
                        rs.getString("rotation"),
                        rs.getBigDecimal("salary"),
                        rs.getInt("department_id")
                );
                nurses.add(nurse);
            }
        } catch (SQLException e) {
            System.out.println("Error getting nurses by department: " + e.getMessage());
        }

        return nurses;
    }
}
