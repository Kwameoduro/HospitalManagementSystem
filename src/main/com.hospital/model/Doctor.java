package com.hospital.model;

/**
 * Represents a Doctor entity in the hospital system.
 * Extends the base Employee class.
 */
public class Doctor extends Employee {
    private String speciality;

    // Default constructor
    public Doctor() {
        super();
    }

    // Constructor for creating new doctors
    public Doctor(String firstName, String lastName, String address, String telephone, String speciality) {
        super(firstName, lastName, address, telephone);
        this.speciality = speciality;
    }

    // Constructor for retrieving existing doctors
    public Doctor(int employeeId, String firstName, String lastName, String address, String telephone, String speciality) {
        super(employeeId, firstName, lastName, address, telephone);
        this.speciality = speciality;
    }

    // Getters and Setters
    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "employeeId=" + getEmployeeId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", telephone='" + getTelephone() + '\'' +
                ", speciality='" + speciality + '\'' +
                '}';
    }
}
