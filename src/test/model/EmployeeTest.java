package model;

import com.hospital.model.Employee;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EmployeeTest {

    @Test
    void testDefaultConstructor() {
        Employee employee = new Employee();
        assertNotNull(employee);
        assertEquals(0, employee.getEmployeeId());
        assertNull(employee.getFirstName());
        assertNull(employee.getLastName());
        assertNull(employee.getAddress());
        assertNull(employee.getTelephone());
    }

    @Test
    void testConstructorWithoutId() {
        Employee employee = new Employee("John", "Doe", "123 Main St", "555-1234");

        assertEquals("John", employee.getFirstName());
        assertEquals("Doe", employee.getLastName());
        assertEquals("123 Main St", employee.getAddress());
        assertEquals("555-1234", employee.getTelephone());
    }

    @Test
    void testConstructorWithId() {
        Employee employee = new Employee(10, "Jane", "Smith", "456 Oak Ave", "555-5678");

        assertEquals(10, employee.getEmployeeId());
        assertEquals("Jane", employee.getFirstName());
        assertEquals("Smith", employee.getLastName());
        assertEquals("456 Oak Ave", employee.getAddress());
        assertEquals("555-5678", employee.getTelephone());
    }

    @Test
    void testSettersAndGetters() {
        Employee employee = new Employee();
        employee.setEmployeeId(20);
        employee.setFirstName("Alice");
        employee.setLastName("Brown");
        employee.setAddress("789 Pine St");
        employee.setTelephone("555-9999");

        assertEquals(20, employee.getEmployeeId());
        assertEquals("Alice", employee.getFirstName());
        assertEquals("Brown", employee.getLastName());
        assertEquals("789 Pine St", employee.getAddress());
        assertEquals("555-9999", employee.getTelephone());
    }

    @Test
    void testGetFullName() {
        Employee employee = new Employee("Sam", "Wilson", "Unknown", "000-0000");
        assertEquals("Sam Wilson", employee.getFullName());
    }

    @Test
    void testToString() {
        Employee employee = new Employee(5, "Emily", "Taylor", "321 Elm St", "555-1111");
        String expected = "Employee{employeeId=5, firstName='Emily', lastName='Taylor', address='321 Elm St', telephone='555-1111'}";
        assertEquals(expected, employee.toString());
    }
}
