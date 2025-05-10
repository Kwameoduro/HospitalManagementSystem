package model;

import com.hospital.model.Doctor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DoctorTest {

    @Test
    void testDefaultConstructor() {
        Doctor doctor = new Doctor();
        assertNotNull(doctor);
        assertNull(doctor.getFirstName());
        assertNull(doctor.getLastName());
        assertNull(doctor.getAddress());
        assertNull(doctor.getTelephone());
        assertNull(doctor.getSpeciality());
    }

    @Test
    void testConstructorWithoutEmployeeId() {
        Doctor doctor = new Doctor("John", "Doe", "123 Main St", "555-1234", "Cardiology");

        assertEquals("John", doctor.getFirstName());
        assertEquals("Doe", doctor.getLastName());
        assertEquals("123 Main St", doctor.getAddress());
        assertEquals("555-1234", doctor.getTelephone());
        assertEquals("Cardiology", doctor.getSpeciality());
    }

    @Test
    void testConstructorWithEmployeeId() {
        Doctor doctor = new Doctor(10, "Jane", "Smith", "456 Oak Ave", "555-5678", "Neurology");

        assertEquals(10, doctor.getEmployeeId());
        assertEquals("Jane", doctor.getFirstName());
        assertEquals("Smith", doctor.getLastName());
        assertEquals("456 Oak Ave", doctor.getAddress());
        assertEquals("555-5678", doctor.getTelephone());
        assertEquals("Neurology", doctor.getSpeciality());
    }

    @Test
    void testSettersAndGetters() {
        Doctor doctor = new Doctor();
        doctor.setEmployeeId(20);
        doctor.setFirstName("Alice");
        doctor.setLastName("Brown");
        doctor.setAddress("789 Pine St");
        doctor.setTelephone("555-9999");
        doctor.setSpeciality("Orthopedics");

        assertEquals(20, doctor.getEmployeeId());
        assertEquals("Alice", doctor.getFirstName());
        assertEquals("Brown", doctor.getLastName());
        assertEquals("789 Pine St", doctor.getAddress());
        assertEquals("555-9999", doctor.getTelephone());
        assertEquals("Orthopedics", doctor.getSpeciality());
    }

    @Test
    void testToString() {
        Doctor doctor = new Doctor(5, "Emily", "Taylor", "321 Elm St", "555-1111", "Dermatology");
        String expected = "Doctor{employeeId=5, firstName='Emily', lastName='Taylor', address='321 Elm St', telephone='555-1111', speciality='Dermatology'}";
        assertEquals(expected, doctor.toString());
    }
}
