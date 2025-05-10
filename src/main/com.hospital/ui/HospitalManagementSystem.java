package com.hospital.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Main application frame for the Hospital Information System.
 * Provides a tabbed interface for managing patients, doctors, and nurses.
 */
public class HospitalManagementSystem extends JFrame {

    private JTabbedPane tabbedPane;

    // Panels for each entity
    private PatientPanel patientPanel;
    private DoctorPanel doctorPanel;
    private NursePanel nursePanel;

    public HospitalManagementSystem() {
        setTitle("Hospital Information System");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Set application icon
        // ImageIcon icon = new ImageIcon(getClass().getResource("/hospital_icon.png"));
        // setIconImage(icon.getImage());

        // Initialize UI components
        initComponents();

        // Center the frame on the screen
        setLocationRelativeTo(null);

        // Make the frame visible
        setVisible(true);
    }

    private void initComponents() {
        // Create main menu
        JMenuBar menuBar = createMenuBar();
        setJMenuBar(menuBar);

        // Create tabbed pane
        tabbedPane = new JTabbedPane();

        // Create panels for each entity
        patientPanel = new PatientPanel();
        doctorPanel = new DoctorPanel();
        nursePanel = new NursePanel();

        // Add panels to tabbed pane
        tabbedPane.addTab("Patients", new ImageIcon(), patientPanel, "Manage patients");
        tabbedPane.addTab("Doctors", new ImageIcon(), doctorPanel, "Manage doctors");
        tabbedPane.addTab("Nurses", new ImageIcon(), nursePanel, "Manage nurses");

        // Add tabbed pane to frame
        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Add status bar
        JPanel statusBar = createStatusBar();
        getContentPane().add(statusBar, BorderLayout.SOUTH);
    }

    private JMenuBar createMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // File menu
        JMenu fileMenu = new JMenu("File");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitItem);

        // Help menu
        JMenu helpMenu = new JMenu("Help");
        JMenuItem aboutItem = new JMenuItem("About");
        aboutItem.addActionListener(e -> showAboutDialog());
        helpMenu.add(aboutItem);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);

        return menuBar;
    }

    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new BorderLayout());
        statusBar.setBorder(BorderFactory.createEtchedBorder());

        JLabel statusLabel = new JLabel("Ready");
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 5));
        statusBar.add(statusLabel, BorderLayout.WEST);

        return statusBar;
    }

    private void showAboutDialog() {
        JOptionPane.showMessageDialog(this,
                "Hospital Information System\nVersion 1.0\n\nA simple CRUD application for hospital management.",
                "About", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        // Set look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Start application
        SwingUtilities.invokeLater(() -> new HospitalManagementSystem());
    }
}
