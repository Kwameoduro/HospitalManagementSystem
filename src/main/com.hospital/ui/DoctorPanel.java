package com.hospital.ui;

import com.hospital.dao.DoctorDAO;
import com.hospital.model.Doctor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * Panel for managing doctors in the hospital system.
 */
public class DoctorPanel extends JPanel {

    private DoctorDAO doctorDAO;
    private JTable doctorTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField telephoneField;
    private JTextField specialityField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    private int selectedDoctorId = -1;

    public DoctorPanel() {
        doctorDAO = new DoctorDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize components
        initComponents();

        // Load initial data
        refreshDoctorTable();
    }

    private void initComponents() {
        // Top panel - Search
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search Doctors: ");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Center panel - Doctor table
        String[] columnNames = {"ID", "First Name", "Last Name", "Address", "Telephone", "Speciality"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        doctorTable = new JTable(tableModel);
        doctorTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        doctorTable.getTableHeader().setReorderingAllowed(false);
        doctorTable.getTableHeader().setResizingAllowed(true);

        JScrollPane scrollPane = new JScrollPane(doctorTable);

        // Bottom panel - Doctor form
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Doctor Details"));

        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(5, 2, 10, 10));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField(20);
        JLabel telephoneLabel = new JLabel("Telephone:");
        telephoneField = new JTextField(20);
        JLabel specialityLabel = new JLabel("Speciality:");
        specialityField = new JTextField(20);

        fieldsPanel.add(firstNameLabel);
        fieldsPanel.add(firstNameField);
        fieldsPanel.add(lastNameLabel);
        fieldsPanel.add(lastNameField);
        fieldsPanel.add(addressLabel);
        fieldsPanel.add(addressField);
        fieldsPanel.add(telephoneLabel);
        fieldsPanel.add(telephoneField);
        fieldsPanel.add(specialityLabel);
        fieldsPanel.add(specialityField);

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
                List<Doctor> doctors = doctorDAO.searchDoctors(searchTerm);
                updateTableWithDoctors(doctors);
            } else {
                refreshDoctorTable();
            }
        });

        // Table row selection
        doctorTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = doctorTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedDoctorId = (int) doctorTable.getValueAt(selectedRow, 0);
                    firstNameField.setText((String) doctorTable.getValueAt(selectedRow, 1));
                    lastNameField.setText((String) doctorTable.getValueAt(selectedRow, 2));
                    addressField.setText((String) doctorTable.getValueAt(selectedRow, 3));
                    telephoneField.setText((String) doctorTable.getValueAt(selectedRow, 4));
                    specialityField.setText((String) doctorTable.getValueAt(selectedRow, 5));

                    // Enable update and delete buttons
                    updateButton.setEnabled(true);
                    deleteButton.setEnabled(true);
                }
            }
        });

        // Add button
        addButton.addActionListener(e -> {
            if (validateForm()) {
                Doctor doctor = new Doctor(
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        addressField.getText().trim(),
                        telephoneField.getText().trim(),
                        specialityField.getText().trim()
                );

                int doctorId = doctorDAO.createDoctor(doctor);
                if (doctorId > 0) {
                    JOptionPane.showMessageDialog(this,
                            "Doctor added successfully with ID: " + doctorId,
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshDoctorTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to add doctor.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Update button
        updateButton.addActionListener(e -> {
            if (validateForm() && selectedDoctorId > 0) {
                Doctor doctor = new Doctor(
                        selectedDoctorId,
                        firstNameField.getText().trim(),
                        lastNameField.getText().trim(),
                        addressField.getText().trim(),
                        telephoneField.getText().trim(),
                        specialityField.getText().trim()
                );

                boolean updated = doctorDAO.updateDoctor(doctor);
                if (updated) {
                    JOptionPane.showMessageDialog(this,
                            "Doctor updated successfully.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    clearForm();
                    refreshDoctorTable();
                } else {
                    JOptionPane.showMessageDialog(this,
                            "Failed to update doctor.",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            if (selectedDoctorId > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this doctor?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = doctorDAO.deleteDoctor(selectedDoctorId);
                    if (deleted) {
                        JOptionPane.showMessageDialog(this,
                                "Doctor deleted successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        refreshDoctorTable();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete doctor.",
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
        specialityField.setText("");
        selectedDoctorId = -1;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        doctorTable.clearSelection();
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

        if (specialityField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Speciality is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            specialityField.requestFocus();
            return false;
        }

        return true;
    }

    private void refreshDoctorTable() {
        List<Doctor> doctors = doctorDAO.readAllDoctors();
        updateTableWithDoctors(doctors);
    }

    private void updateTableWithDoctors(List<Doctor> doctors) {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Add doctors to the table
        for (Doctor doctor : doctors) {
            Object[] rowData = {
                    doctor.getEmployeeId(),
                    doctor.getFirstName(),
                    doctor.getLastName(),
                    doctor.getAddress(),
                    doctor.getTelephone(),
                    doctor.getSpeciality()
            };
            tableModel.addRow(rowData);
        }
    }
}
