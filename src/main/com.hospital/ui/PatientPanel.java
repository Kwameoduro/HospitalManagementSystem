package com.hospital.ui;

import com.hospital.dao.PatientDAO;
import com.hospital.model.Patient;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for managing patients in the hospital system.
 */
public class PatientPanel extends JPanel {

    private PatientDAO patientDAO;
    private JTable patientTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField telephoneField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    private int selectedPatientId = -1;

    public PatientPanel() {
        patientDAO = new PatientDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize components
        initComponents();

        // Load initial data
        refreshPatientTable();
    }

    private void initComponents() {
        // Top panel - Search
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search Patients: ");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Center panel - Patient table
        String[] columnNames = {"ID", "First Name", "Last Name", "Address", "Telephone"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        patientTable = new JTable(tableModel);
        patientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        patientTable.getTableHeader().setReorderingAllowed(false);
        patientTable.getTableHeader().setResizingAllowed(true);

        JScrollPane scrollPane = new JScrollPane(patientTable);

        // Bottom panel - Patient form
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Patient Details"));

        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField(20);
        JLabel telephoneLabel = new JLabel("Telephone:");
        telephoneField = new JTextField(20);

        fieldsPanel.add(firstNameLabel);
        fieldsPanel.add(firstNameField);
        fieldsPanel.add(lastNameLabel);
        fieldsPanel.add(lastNameField);
        fieldsPanel.add(addressLabel);
        fieldsPanel.add(addressField);
        fieldsPanel.add(telephoneLabel);
        fieldsPanel.add(telephoneField);

        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        addButton = new JButton("Add");
        updateButton = new JButton("Update");
        deleteButton = new JButton("Delete");
        clearButton = new JButton("Clear");

        // Initially disable update and delete buttons
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);

        buttonPanel.add(addButton);
        buttonPanel.add(updateButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(clearButton);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
        formPanel.add(buttonPanel, BorderLayout.SOUTH);

        // Add components to main panel
        add(searchPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(formPanel, BorderLayout.SOUTH);

        // Add event listeners

        // Search button
        searchButton.addActionListener(e -> {
            String searchTerm = searchField.getText().trim();
            if (!searchTerm.isEmpty()) {
                List<Patient> patients = patientDAO.searchPatientsByName(searchTerm);
                updateTableWithPatients(patients);
            } else {
                refreshPatientTable();
            }
        });

        // Table row selection
        patientTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = patientTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedPatientId = (int) patientTable.getValueAt(selectedRow, 0);
                    firstNameField.setText((String) patientTable.getValueAt(selectedRow, 1));
                    lastNameField.setText((String) patientTable.getValueAt(selectedRow, 2));
                    addressField.setText((String) patientTable.getValueAt(selectedRow, 3));
                    telephoneField.setText((String) patientTable.getValueAt(selectedRow, 4));

                    // Enable update and delete buttons
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Add button
        addButton.addActionListener(e -> {
            if (validateForm()) {
                Patient patient = new Patient(
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        addressField.getText().trim(),
                        telephoneField.getText().trim()
                );

                int patientId = patientDAO.createPatient(patient);
                if (patientId > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Patient added successfully with ID: " + patientId,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshPatientTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to add patient.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Update button
        updateButton.addActionListener(e -> {
            if (validateForm() && selectedPatientId > 0) {
                Patient patient = new Patient(
                        selectedPatientId,
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        addressField.getText().trim(),
                        telephoneField.getText().trim()
                );

                boolean updated = patientDAO.updatePatient(patient);
                if (updated) {
                    JOptionPane.showMessageDialog(this,
                            "Patient updated successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshPatientTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to update patient.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            if (selectedPatientId > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this patient?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = patientDAO.deletePatient(selectedPatientId);
                    if (deleted) {
                        JOptionPane.showMessageDialog(this,
                                "Patient deleted successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        refreshPatientTable();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete patient.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Clear button
        clearButton.addActionListener(e -> clearForm());
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        telephoneField.setText("");
        selectedPatientId = -1;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        patientTable.clearSelection();
    }

    private boolean validateForm() {
        if (firstNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "First Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            firstNameField.requestFocus();
            return false;
        }

        if (lastNameField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Last Name is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            lastNameField.requestFocus();
            return false;
        }

        return true;
    }

    private void refreshPatientTable() {
        List<Patient> patients = patientDAO.readAllPatients();
        updateTableWithPatients(patients);
    }

    private void updateTableWithPatients(List<Patient> patients) {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Add patients to the table
        for (Patient patient : patients) {
            Object[] rowData = {
                    patient.getPatientId(),
                    patient.getFirstName(),
                    patient.getLastName(),
                    patient.getAddress(),
                    patient.getTelephone()
            };
            tableModel.addRow(rowData);
        }
    }
}
