package com.hospital.dao;

import com.hospital.model.Department;
import com.hospital.util.DatabaseUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Department Data Access Object.
 * Provides CRUD operations for Department entities.
 */
public class DepartmentDAO {

    /**
     * Create a new department record in the database
     * @param department Department object to be added
     * @return the generated department ID if successful, -1 if failed
     */
    public int createDepartment(Department department) {
        String sql = "INSERT INTO department (code, name, building, director_id) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, department.getCode());
            stmt.setString(2, department.getName());
            stmt.setString(3, department.getBuilding());

            if (department.getDirectorId() != null) {
                stmt.setInt(4, department.getDirectorId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            int affectedRows = stmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Creating department failed, no rows affected.");
            }

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int id = generatedKeys.getInt(1);
                    department.setDepartmentId(id);
                    return id;
                } else {
                    throw new SQLException("Creating department failed, no ID obtained.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Error creating department: " + e.getMessage());
            return -1;
        }
    }

    /**
     * Read a department record from the database by ID
     * @param departmentId the ID of the department to retrieve
     * @return the Department object if found, null if not found
     */
    public Department readDepartment(int departmentId) {
        String sql = "SELECT * FROM department WHERE department_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Integer directorId = rs.getObject("director_id", Integer.class); // Handles NULL values

                return new Department(
                        rs.getInt("department_id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("building"),
                        directorId
                );
            } else {
                return null; // Department not found
            }
        } catch (SQLException e) {
            System.out.println("Error reading department: " + e.getMessage());
            return null;
        }
    }

    /**
     * Read all departments from the database
     * @return List of all Department objects
     */
    public List<Department> readAllDepartments() {
        String sql = "SELECT * FROM department";
        List<Department> departments = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Integer directorId = rs.getObject("director_id", Integer.class); // Handles NULL values

                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("building"),
                        directorId
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            System.out.println("Error reading all departments: " + e.getMessage());
        }

        return departments;
    }

    /**
     * Update a department record in the database
     * @param department Department object with updated information
     * @return true if update was successful, false otherwise
     */
    public boolean updateDepartment(Department department) {
        String sql = "UPDATE department SET code = ?, name = ?, building = ?, director_id = ? WHERE department_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, department.getCode());
            stmt.setString(2, department.getName());
            stmt.setString(3, department.getBuilding());

            if (department.getDirectorId() != null) {
                stmt.setInt(4, department.getDirectorId());
            } else {
                stmt.setNull(4, java.sql.Types.INTEGER);
            }

            stmt.setInt(5, department.getDepartmentId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error updating department: " + e.getMessage());
            return false;
        }
    }

    /**
     * Delete a department record from the database
     * @param departmentId the ID of the department to delete
     * @return true if deletion was successful, false otherwise
     */
    public boolean deleteDepartment(int departmentId) {
        String sql = "DELETE FROM department WHERE department_id = ?";

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, departmentId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.out.println("Error deleting department: " + e.getMessage());
            return false;
        }
    }

    /**
     * Search for departments by code or name
     * @param query the code or name to search for
     * @return List of matching Department objects
     */
    public List<Department> searchDepartments(String query) {
        String sql = "SELECT * FROM department WHERE code LIKE ? OR name LIKE ?";
        List<Department> departments = new ArrayList<>();

        try (Connection conn = DatabaseUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            String searchPattern = "%" + query + "%";
            stmt.setString(1, searchPattern);
            stmt.setString(2, searchPattern);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Integer directorId = rs.getObject("director_id", Integer.class); // Handles NULL values

                Department department = new Department(
                        rs.getInt("department_id"),
                        rs.getString("code"),
                        rs.getString("name"),
                        rs.getString("building"),
                        directorId
                );
                departments.add(department);
            }
        } catch (SQLException e) {
            System.out.println("Error searching departments: " + e.getMessage());
        }

        return departments;
    }
}
