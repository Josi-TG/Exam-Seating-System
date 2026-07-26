package examsystem.ui;

import examsystem.service.SeatingService;
import examsystem.model.ExamRoom;
import examsystem.model.Student;
import examsystem.model.ExamSchedule;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class MainFrame extends JFrame {
    private SeatingService service;
    private JTabbedPane tabbedPane;
    
    // Student Panel Components
    private JTextField studentNameField, studentEmailField, studentIdField, studentDeptField;
    private DefaultTableModel studentTableModel;
    private JTable studentTable;
    
    // Room Panel Components
    private JTextField roomNumberField, roomBuildingField, roomCapacityField;
    private DefaultTableModel roomTableModel;
    private JTable roomTable;
    
    // Schedule Panel Components
    private JTextField scheduleCodeField, scheduleNameField, scheduleDurationField;
    private JTextArea scheduleDisplayArea;
    
    // Seating Panel Components
    private JTextArea seatingDisplayArea;
    
    public MainFrame() {
        service = new SeatingService();
        
        setTitle("University Exam Seating Arrangement System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Students", createStudentPanel());
        tabbedPane.addTab("Rooms", createRoomPanel());
        tabbedPane.addTab("Schedules", createSchedulePanel());
        tabbedPane.addTab("Seating Charts", createSeatingPanel());
        tabbedPane.addTab("Reports", createReportPanel());
        
        add(tabbedPane);
        
        // Add sample data for demo
        addSampleData();
        
        setVisible(true);
    }
    
    // ============ STUDENT PANEL ============
    private JPanel createStudentPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(5, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Student"));
        
        inputPanel.add(new JLabel("Name:"));
        studentNameField = new JTextField();
        inputPanel.add(studentNameField);
        
        inputPanel.add(new JLabel("Email:"));
        studentEmailField = new JTextField();
        inputPanel.add(studentEmailField);
        
        inputPanel.add(new JLabel("Student ID:"));
        studentIdField = new JTextField();
        inputPanel.add(studentIdField);
        
        inputPanel.add(new JLabel("Department:"));
        studentDeptField = new JTextField();
        inputPanel.add(studentDeptField);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Student");
        addBtn.addActionListener(e -> addStudent());
        btnPanel.add(addBtn);
        
        JButton clearBtn = new JButton("Clear Fields");
        clearBtn.addActionListener(e -> clearStudentFields());
        btnPanel.add(clearBtn);
        
        inputPanel.add(btnPanel);
        
        // Table Panel
        String[] columns = {"ID", "Name", "Email", "Student ID", "Department"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        studentTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(studentTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Student List"));
        
        // Button Panel below table
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteStudent());
        actionPanel.add(deleteBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshStudentTable());
        actionPanel.add(refreshBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        refreshStudentTable();
        return panel;
    }
    
    // ============ ROOM PANEL ============
    private JPanel createRoomPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Add New Room"));
        
        inputPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        inputPanel.add(roomNumberField);
        
        inputPanel.add(new JLabel("Building:"));
        roomBuildingField = new JTextField();
        inputPanel.add(roomBuildingField);
        
        inputPanel.add(new JLabel("Capacity:"));
        roomCapacityField = new JTextField();
        inputPanel.add(roomCapacityField);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton addBtn = new JButton("Add Room");
        addBtn.addActionListener(e -> addRoom());
        btnPanel.add(addBtn);
        
        JButton clearBtn = new JButton("Clear Fields");
        clearBtn.addActionListener(e -> clearRoomFields());
        btnPanel.add(clearBtn);
        
        inputPanel.add(btnPanel);
        
        String[] columns = {"Room #", "Building", "Capacity", "Available", "Status"};
        roomTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        roomTable = new JTable(roomTableModel);
        roomTable.setRowHeight(25);
        JScrollPane scrollPane = new JScrollPane(roomTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Room List"));
        
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton deleteBtn = new JButton("Delete Selected");
        deleteBtn.addActionListener(e -> deleteRoom());
        actionPanel.add(deleteBtn);
        
        JButton refreshBtn = new JButton("Refresh");
        refreshBtn.addActionListener(e -> refreshRoomTable());
        actionPanel.add(refreshBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        refreshRoomTable();
        return panel;
    }
    
    // ============ SCHEDULE PANEL ============
    private JPanel createSchedulePanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JPanel inputPanel = new JPanel(new GridLayout(4, 2, 10, 5));
        inputPanel.setBorder(BorderFactory.createTitledBorder("Create Exam Schedule"));
        
        inputPanel.add(new JLabel("Course Code:"));
        scheduleCodeField = new JTextField();
        inputPanel.add(scheduleCodeField);
        
        inputPanel.add(new JLabel("Course Name:"));
        scheduleNameField = new JTextField();
        inputPanel.add(scheduleNameField);
        
        inputPanel.add(new JLabel("Duration (minutes):"));
        scheduleDurationField = new JTextField();
        inputPanel.add(scheduleDurationField);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton createBtn = new JButton("Create Schedule");
        createBtn.addActionListener(e -> createSchedule());
        btnPanel.add(createBtn);
        
        JButton enrollBtn = new JButton("Enroll Student");
        enrollBtn.addActionListener(e -> enrollStudent());
        btnPanel.add(enrollBtn);
        
        inputPanel.add(btnPanel);
        
        scheduleDisplayArea = new JTextArea();
        scheduleDisplayArea.setEditable(false);
        scheduleDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(scheduleDisplayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Exam Schedules"));
        
        JPanel actionPanel = new JPanel(new FlowLayout());
        JButton generateBtn = new JButton("Generate All Seating");
        generateBtn.addActionListener(e -> generateSeating());
        actionPanel.add(generateBtn);
        
        JButton refreshBtn = new JButton("Refresh Schedules");
        refreshBtn.addActionListener(e -> refreshScheduleDisplay());
        actionPanel.add(refreshBtn);
        
        panel.add(inputPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(actionPanel, BorderLayout.SOUTH);
        
        refreshScheduleDisplay();
        return panel;
    }
    
    // ============ SEATING PANEL ============
    private JPanel createSeatingPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        seatingDisplayArea = new JTextArea();
        seatingDisplayArea.setEditable(false);
        seatingDisplayArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(seatingDisplayArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Seating Charts"));
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton showBtn = new JButton("Show Seating Charts");
        showBtn.addActionListener(e -> displaySeatingCharts());
        btnPanel.add(showBtn);
        
        JButton clearBtn = new JButton("Clear Display");
        clearBtn.addActionListener(e -> seatingDisplayArea.setText(""));
        btnPanel.add(clearBtn);
        
        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============ REPORT PANEL ============
    private JPanel createReportPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        JTextArea reportArea = new JTextArea();
        reportArea.setEditable(false);
        reportArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane scrollPane = new JScrollPane(reportArea);
        
        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton generateBtn = new JButton("Generate Report");
        generateBtn.addActionListener(e -> reportArea.setText(service.getSummary()));
        btnPanel.add(generateBtn);
        
        JButton clearBtn = new JButton("Clear");
        clearBtn.addActionListener(e -> reportArea.setText(""));
        btnPanel.add(clearBtn);
        
        panel.add(btnPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // ============ ACTION METHODS ============
    
    private void addStudent() {
        String name = studentNameField.getText().trim();
        String email = studentEmailField.getText().trim();
        String studentId = studentIdField.getText().trim();
        String dept = studentDeptField.getText().trim();
        
        if (name.isEmpty() || email.isEmpty() || studentId.isEmpty() || dept.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        service.addStudent(name, email, studentId, dept);
        clearStudentFields();
        refreshStudentTable();
        JOptionPane.showMessageDialog(this, "Student added successfully!");
    }
    
    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a student to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        String studentId = (String) studentTableModel.getValueAt(selectedRow, 3);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this student?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteStudent(studentId);
            refreshStudentTable();
            JOptionPane.showMessageDialog(this, "Student deleted!");
        }
    }
    
    private void clearStudentFields() {
        studentNameField.setText("");
        studentEmailField.setText("");
        studentIdField.setText("");
        studentDeptField.setText("");
    }
    
    private void refreshStudentTable() {
        studentTableModel.setRowCount(0);
        for (Student s : service.getAllStudents()) {
            studentTableModel.addRow(new Object[]{
                s.getId(), s.getName(), s.getEmail(), s.getStudentId(), s.getDepartment()
            });
        }
    }
    
    private void addRoom() {
        try {
            int roomNumber = Integer.parseInt(roomNumberField.getText().trim());
            String building = roomBuildingField.getText().trim();
            int capacity = Integer.parseInt(roomCapacityField.getText().trim());
            
            if (building.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter building name!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            service.addRoom(roomNumber, building, capacity);
            clearRoomFields();
            refreshRoomTable();
            JOptionPane.showMessageDialog(this, "Room added successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid numbers!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteRoom() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a room to delete!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        int roomNumber = (int) roomTableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Delete this room?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            service.deleteRoom(roomNumber);
            refreshRoomTable();
            JOptionPane.showMessageDialog(this, "Room deleted!");
        }
    }
    
    private void clearRoomFields() {
        roomNumberField.setText("");
        roomBuildingField.setText("");
        roomCapacityField.setText("");
    }
    
    private void refreshRoomTable() {
        roomTableModel.setRowCount(0);
        for (ExamRoom r : service.getAllRooms()) {
            roomTableModel.addRow(new Object[]{
                r.getRoomNumber(), r.getBuilding(), r.getCapacity(), 
                r.getAvailableSeats(), r.isFull() ? "Full" : "Available"
            });
        }
    }
    
    private void createSchedule() {
        String code = scheduleCodeField.getText().trim();
        String name = scheduleNameField.getText().trim();
        String durationStr = scheduleDurationField.getText().trim();
        
        if (code.isEmpty() || name.isEmpty() || durationStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        try {
            int duration = Integer.parseInt(durationStr);
            service.createSchedule(code, name, LocalDateTime.now().plusDays(7), duration);
            clearScheduleFields();
            refreshScheduleDisplay();
            JOptionPane.showMessageDialog(this, "Schedule created successfully!");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter valid duration!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void enrollStudent() {
        String studentId = JOptionPane.showInputDialog(this, "Enter Student ID:");
        if (studentId == null || studentId.trim().isEmpty()) return;
        
        String courseCode = JOptionPane.showInputDialog(this, "Enter Course Code:");
        if (courseCode == null || courseCode.trim().isEmpty()) return;
        
        if (service.enrollStudentInExam(studentId.trim(), courseCode.trim())) {
            refreshScheduleDisplay();
            JOptionPane.showMessageDialog(this, "Student enrolled successfully!");
        } else {
            JOptionPane.showMessageDialog(this, "Student or Course not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void clearScheduleFields() {
        scheduleCodeField.setText("");
        scheduleNameField.setText("");
        scheduleDurationField.setText("");
    }
    
    private void refreshScheduleDisplay() {
        StringBuilder sb = new StringBuilder();
        for (ExamSchedule s : service.getAllSchedules()) {
            sb.append(s.getScheduleInfo());
            sb.append("\n" + "=".repeat(50) + "\n");
        }
        if (sb.length() == 0) {
            sb.append("No schedules created yet.\n");
        }
        scheduleDisplayArea.setText(sb.toString());
    }
    
    private void generateSeating() {
        if (service.getAllSchedules().isEmpty()) {
            JOptionPane.showMessageDialog(this, "No schedules to generate seating for!", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        service.generateSeatingForAllExams();
        refreshScheduleDisplay();
        JOptionPane.showMessageDialog(this, "Seating generated for all exams!");
    }
    
    private void displaySeatingCharts() {
        StringBuilder sb = new StringBuilder();
        sb.append("=".repeat(60)).append("\n");
        sb.append("         EXAM SEATING CHARTS").append("\n");
        sb.append("=".repeat(60)).append("\n");
        
        for (ExamSchedule s : service.getAllSchedules()) {
            sb.append(s.getScheduleInfo());
            sb.append("\n");
        }
        
        if (sb.length() == 0) {
            sb.append("No schedules created yet.\n");
        }
        seatingDisplayArea.setText(sb.toString());
    }
    
    // ============ SAMPLE DATA ============
    private void addSampleData() {
        // Add sample students
        service.addStudent("Alice Johnson", "alice@email.com", "S001", "Computer Science");
        service.addStudent("Bob Smith", "bob@email.com", "S002", "Engineering");
        service.addStudent("Charlie Brown", "charlie@email.com", "S003", "Mathematics");
        service.addStudent("Diana Prince", "diana@email.com", "S004", "Physics");
        service.addStudent("Ethan Hunt", "ethan@email.com", "S005", "Computer Science");
        service.addStudent("Fiona Apple", "fiona@email.com", "S006", "Biology");
        
        // Add sample rooms
        service.addRoom(101, "Science Building", 30);
        service.addRoom(102, "Science Building", 25);
        service.addRoom(201, "Engineering Building", 40);
        service.addRoom(202, "Engineering Building", 20);
        
        // Add sample schedules
        service.createSchedule("CS101", "Intro to Programming", LocalDateTime.now().plusDays(3), 120);
        service.createSchedule("CS201", "Data Structures", LocalDateTime.now().plusDays(5), 90);
        service.createSchedule("MATH101", "Calculus I", LocalDateTime.now().plusDays(7), 150);
        
        // Enroll sample students
        service.enrollStudentInExam("S001", "CS101");
        service.enrollStudentInExam("S002", "CS101");
        service.enrollStudentInExam("S003", "CS101");
        service.enrollStudentInExam("S004", "CS201");
        service.enrollStudentInExam("S005", "CS201");
        service.enrollStudentInExam("S006", "MATH101");
        service.enrollStudentInExam("S001", "MATH101");
        service.enrollStudentInExam("S003", "MATH101");
        
        // Generate seating
        service.generateSeatingForAllExams();
        
        // Refresh all displays
        refreshStudentTable();
        refreshRoomTable();
        refreshScheduleDisplay();
    }
    
    // ============ MAIN ============
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MainFrame());
    }
}