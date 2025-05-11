CREATE DATABASE hospital_info_system;
USE hospital_info_system;
-- Employee table (parent for Doctor and Nurse)
CREATE TABLE employee (
    employee_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    telephone VARCHAR(15)
);
-- Department table
CREATE TABLE department (
    department_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(10) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,
    building VARCHAR(50),
    director_id INT
);
-- Doctor table (inherits from Employee)
CREATE TABLE doctor (
    employee_id INT PRIMARY KEY,
    speciality VARCHAR(100) NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE
);
-- Add foreign key constraint to Department for director
ALTER TABLE department
ADD CONSTRAINT fk_department_director
FOREIGN KEY (director_id) REFERENCES doctor(employee_id) ON DELETE SET NULL;
-- Nurse table (inherits from Employee)
CREATE TABLE nurse (
    employee_id INT PRIMARY KEY,
    rotation VARCHAR(50),
    salary DECIMAL(10, 2),
    department_id INT NOT NULL,
    FOREIGN KEY (employee_id) REFERENCES employee(employee_id) ON DELETE CASCADE,
    FOREIGN KEY (department_id) REFERENCES department(department_id)
);
-- Ward table
CREATE TABLE ward (
    ward_id INT AUTO_INCREMENT PRIMARY KEY,
    ward_number INT NOT NULL,
    bed_count INT NOT NULL,
    supervisor_id INT,
    department_id INT NOT NULL,
    FOREIGN KEY (supervisor_id) REFERENCES nurse(employee_id) ON DELETE SET NULL,
    FOREIGN KEY (department_id) REFERENCES department(department_id),
    UNIQUE (ward_number, department_id) -- Ensures ward numbers are unique within departments
);
-- Patient table
CREATE TABLE patient (
    patient_id INT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    address VARCHAR(255),
    telephone VARCHAR(15)
);
-- Hospitalization table (records patient stays)
CREATE TABLE hospitalization (
    hospitalization_id INT AUTO_INCREMENT PRIMARY KEY,
    patient_id INT NOT NULL,
    ward_id INT NOT NULL,
    bed_number INT NOT NULL,
    diagnosis VARCHAR(255),
    doctor_id INT NOT NULL,
    admission_date DATE NOT NULL,
    discharge_date DATE,
    FOREIGN KEY (patient_id) REFERENCES patient(patient_id),
    FOREIGN KEY (ward_id) REFERENCES ward(ward_id),
    FOREIGN KEY (doctor_id) REFERENCES doctor(employee_id),
    UNIQUE (ward_id, bed_number, admission_date) -- Ensures no bed double-booking
);











-- Insert sample departments
INSERT INTO department (code, name, building) VALUES
('CARD', 'Cardiology', 'Building A'),
('NEUR', 'Neurology', 'Building B'),
('PEDI', 'Pediatrics', 'Building C');
-- Insert sample employees
INSERT INTO employee (first_name, last_name, address, telephone) VALUES
('John', 'Mensa', 'PLT No. 223, KSI', '0240-123-456'),
('Appiah', 'Minkah', 'Ejisu 113 Ave', '0240-321-654'),
('Steelman', 'Bender', 'Tech Ave 114', '0240-112-112'),
('Kwaa', 'Pia', 'Bomso Road 112', '0240-321-321'),
('Abena', 'Memuna', 'Ahodwo Est 133', '0240-191-000');
-- Insert doctors (using employee IDs)
INSERT INTO doctor (employee_id, speciality) VALUES
(1, 'Cardiology'),
(2, 'Neurology');
-- Update departments with directors
UPDATE department SET director_id = 1 WHERE code = 'CARD';
UPDATE department SET director_id = 2 WHERE code = 'NEUR';
-- Insert nurses
INSERT INTO nurse (employee_id, rotation, salary, department_id) VALUES
(3, 'Morning', 65000.00, 1),
(4, 'Evening', 67000.00, 2),
(5, 'Night', 70000.00, 3);
-- Insert wards
INSERT INTO ward (ward_number, bed_count, supervisor_id, department_id) VALUES
(1, 10, 3, 1),
(2, 8, 4, 2),
(1, 12, 5, 3);
-- Insert patients
INSERT INTO patient (first_name, last_name, address, telephone) VALUES
('Koo', 'Poku', 'BLK No. 32, Juaben ', '0240-222-111'),
('Ama', 'Mansa', 'Ahodwo 113, KSI', '0240-111-222'),
('Kofi', 'Peter', 'PLT No. 32, KSI', '0240-111-334');
-- Insert hospitalizations
INSERT INTO hospitalization (patient_id, ward_id, bed_number, diagnosis, doctor_id, admission_date, discharge_date) VALUES
(1, 1, 3, 'Hypertension', 1, '2024-03-01', '2024-03-07'),
(2, 2, 5, 'Migraine', 2, '2024-03-10', NULL),
(3, 3, 7, 'Pneumonia', 1, '2024-03-15', '2024-03-22');
