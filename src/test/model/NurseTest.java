package model;

import com.hospital.model.Nurse;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

class NurseTest {

    @Test
    void testDefaultConstructor() {
        Nurse nurse = new Nurse();
        assertNotNull(nurse);
        assertNull(nurse.getFirstName());
        assertNull(nurse.getLastName());
        assertNull(nurse.getAddress());
        assertNull(nurse.getTelephone());
        assertNull(nurse.getRotation());
        assertNull(nurse.getSalary());
        assertEquals(0, nurse.getDepartmentId());
    }

    @Test
    void testConstructorWithoutEmployeeId() {
        Nurse nurse = new Nurse("John", "Doe", "123 Main St", "555-1234", "Night", new BigDecimal("4500.00"), 3);

        assertEquals("John", nurse.getFirstName());
        assertEquals("Doe", nurse.getLastName());
        assertEquals("123 Main St", nurse.getAddress());
        assertEquals("555-1234", nurse.getTelephone());
        assertEquals("Night", nurse.getRotation());
        assertEquals(new BigDecimal("4500.00"), nurse.getSalary());
        assertEquals(3, nurse.getDepartmentId());
    }

    @Test
    void testConstructorWithEmployeeId() {
        Nurse nurse = new Nurse(10, "Alice", "Smith", "456 Oak Ave", "555-5678", "Day", new BigDecimal("5200.75"), 2);

        assertEquals(10, nurse.getEmployeeId());
        assertEquals("Alice", nurse.getFirstName());
        assertEquals("Smith", nurse.getLastName());
        assertEquals("456 Oak Ave", nurse.getAddress());
        assertEquals("555-5678", nurse.getTelephone());
        assertEquals("Day", nurse.getRotation());
        assertEquals(new BigDecimal("5200.75"), nurse.getSalary());
        assertEquals(2, nurse.getDepartmentId());
    }

    @Test
    void testSettersAndGetters() {
        Nurse nurse = new Nurse();
        nurse.setEmployeeId(5);
        nurse.setFirstName("Emily");
        nurse.setLastName("Brown");
        nurse.setAddress("789 Pine St");
        nurse.setTelephone("555-9999");
        nurse.setRotation("Evening");
        nurse.setSalary(new BigDecimal("6000.50"));
        nurse.setDepartmentId(4);

        assertEquals(5, nurse.getEmployeeId());
        assertEquals("Emily", nurse.getFirstName());
        assertEquals("Brown", nurse.getLastName());
        assertEquals("789 Pine St", nurse.getAddress());
        assertEquals("555-9999", nurse.getTelephone());
        assertEquals("Evening", nurse.getRotation());
        assertEquals(new BigDecimal("6000.50"), nurse.getSalary());
        assertEquals(4, nurse.getDepartmentId());
    }

    @Test
    void testToString() {
        Nurse nurse = new Nurse(7, "Laura", "Wilson", "321 Elm St", "555-0000", "Night", new BigDecimal("4700.00"), 1);
        String expected = "Nurse{employeeId=7, firstName='Laura', lastName='Wilson', address='321 Elm St', telephone='555-0000', rotation='Night', salary=4700.00, departmentId=1}";
        assertEquals(expected, nurse.toString());
    }
}
