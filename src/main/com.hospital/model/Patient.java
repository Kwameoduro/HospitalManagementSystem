package com.hospital.model;

/**
 * Represents a Patient entity in the hospital system.
 */
public class Patient {
    private int patientId;
    private String firstName;
    private String lastName;
    private String address;
    private String telephone;

    // Default constructor
    public Patient() {
    }

    // Constructor with all fields except ID (for creating new patients)
    public Patient(String firstName, String lastName, String address, String telephone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
    }

    // Constructor with all fields (for retrieving existing patients)
    public Patient(int patientId, String firstName, String lastName, String address, String telephone) {
        this.patientId = patientId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.telephone = telephone;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", address='" + address + '\'' +
                ", telephone='" + telephone + '\'' +
                '}';
    }
}
