package model;

import com.hospital.model.Department;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DepartmentTest {

    @Test
    void testDefaultConstructor() {
        Department department = new Department();
        assertNotNull(department);
        assertEquals(0, department.getDepartmentId());
        assertNull(department.getCode());
        assertNull(department.getName());
        assertNull(department.getBuilding());
        assertNull(department.getDirectorId());
    }

    @Test
    void testConstructorWithoutId() {
        Department department = new Department("CARD", "Cardiology", "Building A", 101);

        assertEquals("CARD", department.getCode());
        assertEquals("Cardiology", department.getName());
        assertEquals("Building A", department.getBuilding());
        assertEquals(101, department.getDirectorId());
    }

    @Test
    void testConstructorWithAllFields() {
        Department department = new Department(1, "NEUR", "Neurology", "Building B", null);

        assertEquals(1, department.getDepartmentId());
        assertEquals("NEUR", department.getCode());
        assertEquals("Neurology", department.getName());
        assertEquals("Building B", department.getBuilding());
        assertNull(department.getDirectorId());
    }

    @Test
    void testSettersAndGetters() {
        Department department = new Department();

        department.setDepartmentId(5);
        department.setCode("ORTH");
        department.setName("Orthopedics");
        department.setBuilding("Building C");
        department.setDirectorId(201);

        assertEquals(5, department.getDepartmentId());
        assertEquals("ORTH", department.getCode());
        assertEquals("Orthopedics", department.getName());
        assertEquals("Building C", department.getBuilding());
        assertEquals(201, department.getDirectorId());
    }

    @Test
    void testToStringWithDirector() {
        Department department = new Department(3, "SURG", "Surgery", "Main Wing", 150);
        String expected = "Department{departmentId=3, code='SURG', name='Surgery', building='Main Wing', directorId=150}";
        assertEquals(expected, department.toString());
    }

    @Test
    void testToStringWithoutDirector() {
        Department department = new Department(4, "DERM", "Dermatology", "West Wing", null);
        String expected = "Department{departmentId=4, code='DERM', name='Dermatology', building='West Wing', directorId=null}";
        assertEquals(expected, department.toString());
    }
}
