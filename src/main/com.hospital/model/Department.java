package com.hospital.model;

/**
 * Represents a Department entity in the hospital system.
 */
public class Department {
    private int departmentId;
    private String code;
    private String name;
    private String building;
    private Integer directorId; // Optional, can be null

    // Default constructor
    public Department() {
    }

    // Constructor for creating new departments
    public Department(String code, String name, String building, Integer directorId) {
        this.code = code;
        this.name = name;
        this.building = building;
        this.directorId = directorId;
    }

    // Constructor for retrieving existing departments
    public Department(int departmentId, String code, String name, String building, Integer directorId) {
        this.departmentId = departmentId;
        this.code = code;
        this.name = name;
        this.building = building;
        this.directorId = directorId;
    }

    // Getters and Setters
    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public Integer getDirectorId() {
        return directorId;
    }

    public void setDirectorId(Integer directorId) {
        this.directorId = directorId;
    }

    @Override
    public String toString() {
        return "Department{" +
                "departmentId=" + departmentId +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", building='" + building + '\'' +
                ", directorId=" + directorId +
                '}';
    }
}
