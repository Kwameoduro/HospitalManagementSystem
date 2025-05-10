package com.hospital.ui;

import com.hospital.dao.DepartmentDAO;
import com.hospital.dao.NurseDAO;
import com.hospital.model.Department;
import com.hospital.model.Nurse;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.math.BigDecimal;
import java.util.List;

/**
 * Panel for managing nurses in the hospital system.
 */
public class NursePanel extends JPanel {

    private NurseDAO nurseDAO;
    private DepartmentDAO departmentDAO;
    private JTable nurseTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField addressField;
    private JTextField telephoneField;
    private JTextField rotationField;
    private JTextField salaryField;
    private JComboBox<ComboItem> departmentCombo;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;

    private int selectedNurseId = -1;

    public NursePanel() {
        nurseDAO = new NurseDAO();
        departmentDAO = new DepartmentDAO();
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // Initialize components
        initComponents();

        // Load initial data
        refreshNurseTable();
    }

    private void initComponents() {
        // Top panel - Search
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        JLabel searchLabel = new JLabel("Search Nurses: ");
        searchField = new JTextField(20);
        JButton searchButton = new JButton("Search");

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        // Center panel - Nurse table
        String[] columnNames = {"ID", "First Name", "Last Name", "Address", "Telephone", "Rotation", "Salary", "Department"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make table non-editable
            }
        };

        nurseTable = new JTable(tableModel);
        nurseTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        nurseTable.getTableHeader().setReorderingAllowed(false);
        nurseTable.getTableHeader().setResizingAllowed(true);

        JScrollPane scrollPane = new JScrollPane(nurseTable);

        // Bottom panel - Nurse form
        JPanel formPanel = new JPanel(new BorderLayout(10, 10));
        formPanel.setBorder(BorderFactory.createTitledBorder("Nurse Details"));

        // Form fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(7, 2, 10, 10));

        JLabel firstNameLabel = new JLabel("First Name:");
        firstNameField = new JTextField(20);
        JLabel lastNameLabel = new JLabel("Last Name:");
        lastNameField = new JTextField(20);
        JLabel addressLabel = new JLabel("Address:");
        addressField = new JTextField(20);
        JLabel telephoneLabel = new JLabel("Telephone:");
        telephoneField = new JTextField(20);
        JLabel rotationLabel = new JLabel("Rotation:");
        rotationField = new JTextField(20);
        JLabel salaryLabel = new JLabel("Salary:");
        salaryField = new JTextField(20);
        JLabel departmentLabel = new JLabel("Department:");
        departmentCombo = new JComboBox<>();

        // Populate department combo box
        populateDepartmentCombo();

        fieldsPanel.add(firstNameLabel);
        fieldsPanel.add(firstNameField);
        fieldsPanel.add(lastNameLabel);
        fieldsPanel.add(lastNameField);
        fieldsPanel.add(addressLabel);
        fieldsPanel.add(addressField);
        fieldsPanel.add(telephoneLabel);
        fieldsPanel.add(telephoneField);
        fieldsPanel.add(rotationLabel);
        fieldsPanel.add(rotationField);
        fieldsPanel.add(salaryLabel);
        fieldsPanel.add(salaryField);
        fieldsPanel.add(departmentLabel);
        fieldsPanel.add(departmentCombo);

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
                List<Nurse> nurses = nurseDAO.searchNurses(searchTerm);
                updateTableWithNurses(nurses);
            } else {
                refreshNurseTable();
            }
        });

        // Table row selection
        nurseTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = nurseTable.getSelectedRow();
                if (selectedRow >= 0) {
                    selectedNurseId = (int) nurseTable.getValueAt(selectedRow, 0);

                    // Retrieve the nurse data from the database to get all fields
                    Nurse nurse = nurseDAO.readNurse(selectedNurseId);
                    if (nurse != null) {
                        firstNameField.setText(nurse.getFirstName());
                        lastNameField.setText(nurse.getLastName());
                        addressField.setText(nurse.getAddress());
                        telephoneField.setText(nurse.getTelephone());
                        rotationField.setText(nurse.getRotation());
                        salaryField.setText(nurse.getSalary().toString());

                        // Select the correct department in the combo box
                        for (int i = 0; i < departmentCombo.getItemCount(); i++) {
                            ComboItem item = departmentCombo.getItemAt(i);
                            if (item.getValue() == nurse.getDepartmentId()) {
                                departmentCombo.setSelectedIndex(i);
                                break;
                            }
                        }

                        // Enable update and delete buttons
                        updateButton.setEnabled(true);
                        deleteButton.setEnabled(true);
                    }
                }
            }
        });

        // Add button
        addButton.addActionListener(e -> {
            if (validateForm()) {
                try {
                    ComboItem selectedDepartment = (ComboItem) departmentCombo.getSelectedItem();

                    Nurse nurse = new Nurse(
                            firstNameField.getText().trim(),
                            lastNameField.getText().trim(),
                            addressField.getText().trim(),
                            telephoneField.getText().trim(),
                            rotationField.getText().trim(),
                            new BigDecimal(salaryField.getText().trim()),
                            selectedDepartment.getValue()
                    );

                    int nurseId = nurseDAO.createNurse(nurse);
                    if (nurseId > 0) {
                        JOptionPane.showMessageDialog(this,
                                "Nurse added successfully with ID: " + nurseId,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        refreshNurseTable();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to add nurse.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid salary format. Please enter a valid number.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    salaryField.requestFocus();
                }
            }
        });

        // Update button
        updateButton.addActionListener(e -> {
            if (validateForm() && selectedNurseId > 0) {
                try {
                    ComboItem selectedDepartment = (ComboItem) departmentCombo.getSelectedItem();

                    Nurse nurse = new Nurse(
                            selectedNurseId,
                            firstNameField.getText().trim(),
                            lastNameField.getText().trim(),
                            addressField.getText().trim(),
                            telephoneField.getText().trim(),
                            rotationField.getText().trim(),
                            new BigDecimal(salaryField.getText().trim()),
                            selectedDepartment.getValue()
                    );

                    boolean updated = nurseDAO.updateNurse(nurse);
                    if (updated) {
                        JOptionPane.showMessageDialog(this,
                                "Nurse updated successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        refreshNurseTable();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to update nurse.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this,
                            "Invalid salary format. Please enter a valid number.",
                            "Validation Error", JOptionPane.ERROR_MESSAGE);
                    salaryField.requestFocus();
                }
            }
        });

        // Delete button
        deleteButton.addActionListener(e -> {
            if (selectedNurseId > 0) {
                int confirm = JOptionPane.showConfirmDialog(this,
                        "Are you sure you want to delete this nurse?",
                        "Confirm Delete", JOptionPane.YES_NO_OPTION);

                if (confirm == JOptionPane.YES_OPTION) {
                    boolean deleted = nurseDAO.deleteNurse(selectedNurseId);
                    if (deleted) {
                        JOptionPane.showMessageDialog(this,
                                "Nurse deleted successfully.",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        clearForm();
                        refreshNurseTable();
                    } else {
                        JOptionPane.showMessageDialog(this,
                                "Failed to delete nurse.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        // Clear button
        clearButton.addActionListener(e -> clearForm());
    }

    private void populateDepartmentCombo() {
        departmentCombo.removeAllItems();

        List<Department> departments = departmentDAO.readAllDepartments();
        for (Department department : departments) {
            departmentCombo.addItem(new ComboItem(department.getName(), department.getDepartmentId()));
        }
    }

    private void clearForm() {
        firstNameField.setText("");
        lastNameField.setText("");
        addressField.setText("");
        telephoneField.setText("");
        rotationField.setText("");
        salaryField.setText("");
        departmentCombo.setSelectedIndex(departmentCombo.getItemCount() > 0 ? 0 : -1);
        selectedNurseId = -1;
        updateButton.setEnabled(false);
        deleteButton.setEnabled(false);
        nurseTable.clearSelection();
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

        if (rotationField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Rotation is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            rotationField.requestFocus();
            return false;
        }

        if (salaryField.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Salary is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            salaryField.requestFocus();
            return false;
        }

        try {
            new BigDecimal(salaryField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid salary format", "Validation Error", JOptionPane.ERROR_MESSAGE);
            salaryField.requestFocus();
            return false;
        }

        if (departmentCombo.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(this, "Department is required", "Validation Error", JOptionPane.ERROR_MESSAGE);
            departmentCombo.requestFocus();
            return false;
        }

        return true;
    }

    private void refreshNurseTable() {
        List<Nurse> nurses = nurseDAO.readAllNurses();
        updateTableWithNurses(nurses);
    }

    private void updateTableWithNurses(List<Nurse> nurses) {
        // Clear existing rows
        tableModel.setRowCount(0);

        // Add nurses to the table
        for (Nurse nurse : nurses) {
            // Get department name for the nurse's department ID
            Department department = departmentDAO.readDepartment(nurse.getDepartmentId());
            String departmentName = department != null ? department.getName() : "Unknown";

            Object[] rowData = {
                    nurse.getEmployeeId(),
                    nurse.getFirstName(),
                    nurse.getLastName(),
                    nurse.getAddress(),
                    nurse.getTelephone(),
                    nurse.getRotation(),
                    nurse.getSalary(),
                    departmentName
            };
            tableModel.addRow(rowData);
        }
    }

    // Helper class for ComboBox items
    private class ComboItem {
        private String text;
        private int value;

        public ComboItem(String text, int value) {
            this.text = text;
            this.value = value;
        }

        public int getValue() {
            return value;
        }

        @Override
        public String toString() {
            return text;
        }
    }
}
