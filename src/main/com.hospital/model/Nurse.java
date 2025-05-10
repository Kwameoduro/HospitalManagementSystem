package com.hospital.model;

import java.math.BigDecimal;

/**
 * Represents a Nurse entity in the hospital system.
 * Extends the base Employee class.
 */
public class Nurse extends Employee {
    private String rotation;
    private BigDecimal salary;
    private int departmentId;

    // Default constructor
    public Nurse() {
        super();
    }

    // Constructor for creating new nurses
    public Nurse(String firstName, String lastName, String address, String telephone, String rotation, BigDecimal salary, int departmentId) {
        super(firstName, lastName, address, telephone);
        this.rotation = rotation;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    // Constructor for retrieving existing nurses
    public Nurse(int employeeId, String firstName, String lastName, String address, String telephone, String rotation, BigDecimal salary, int departmentId) {
        super(employeeId, firstName, lastName, address, telephone);
        this.rotation = rotation;
        this.salary = salary;
        this.departmentId = departmentId;
    }

    // Getters and Setters
    public String getRotation() {
        return rotation;
    }

    public void setRotation(String rotation) {
        this.rotation = rotation;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "employeeId=" + getEmployeeId() +
                ", firstName='" + getFirstName() + '\'' +
                ", lastName='" + getLastName() + '\'' +
                ", address='" + getAddress() + '\'' +
                ", telephone='" + getTelephone() + '\'' +
                ", rotation='" + rotation + '\'' +
                ", salary=" + salary +
                ", departmentId=" + departmentId +
                '}';
    }
}
