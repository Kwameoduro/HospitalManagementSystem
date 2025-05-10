package model;

import com.hospital.model.Patient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PatientTest {

    @Test
    void testDefaultConstructor() {
        Patient patient = new Patient();
        assertNotNull(patient);
        assertEquals(0, patient.getPatientId());
        assertNull(patient.getFirstName());
        assertNull(patient.getLastName());
        assertNull(patient.getAddress());
        assertNull(patient.getTelephone());
    }

    @Test
    void testConstructorWithoutId() {
        Patient patient = new Patient("Jane", "Doe", "123 Maple St", "555-1234");

        assertEquals("Jane", patient.getFirstName());
        assertEquals("Doe", patient.getLastName());
        assertEquals("123 Maple St", patient.getAddress());
        assertEquals("555-1234", patient.getTelephone());
    }

    @Test
    void testConstructorWithId() {
        Patient patient = new Patient(1, "John", "Smith", "456 Oak Ave", "555-5678");

        assertEquals(1, patient.getPatientId());
        assertEquals("John", patient.getFirstName());
        assertEquals("Smith", patient.getLastName());
        assertEquals("456 Oak Ave", patient.getAddress());
        assertEquals("555-5678", patient.getTelephone());
    }

    @Test
    void testSettersAndGetters() {
        Patient patient = new Patient();
        patient.setPatientId(2);
        patient.setFirstName("Alice");
        patient.setLastName("Johnson");
        patient.setAddress("789 Pine Rd");
        patient.setTelephone("555-9012");

        assertEquals(2, patient.getPatientId());
        assertEquals("Alice", patient.getFirstName());
        assertEquals("Johnson", patient.getLastName());
        assertEquals("789 Pine Rd", patient.getAddress());
        assertEquals("555-9012", patient.getTelephone());
    }

    @Test
    void testToString() {
        Patient patient = new Patient(3, "Bob", "White", "101 Elm St", "555-0000");
        String expected = "Patient{patientId=3, firstName='Bob', lastName='White', address='101 Elm St', telephone='555-0000'}";
        assertEquals(expected, patient.toString());
    }
}
