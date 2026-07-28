package examsystem.ui;

import examsystem.service.SeatingService;
import examsystem.model.ExamRoom;
import examsystem.model.Student;
import examsystem.model.ExamSchedule;
import examsystem.ui.theme.RoundedButton;
import examsystem.ui.theme.Theme;
import examsystem.ui.theme.UIStyle;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDateTime;

public class MainFrame extends JFrame {
    private final SeatingService service;
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
        applyGlobalDefaults();

        setTitle("University Exam Seating Arrangement System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 720);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BACKGROUND);

        tabbedPane = new JTabbedPane();
        UIStyle.styleTabbedPane(tabbedPane);
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

    /** Sets a few sane Swing-wide defaults so dialogs and tooltips match the rest of the app. */
    private void applyGlobalDefaults() {
        UIManager.put("OptionPane.background", Theme.SURFACE);
        UIManager.put("Panel.background", Theme.SURFACE);
        UIManager.put("OptionPane.messageFont", Theme.FONT_BODY);
        UIManager.put("ToolTip.background", Theme.TEXT_PRIMARY);
        UIManager.put("ToolTip.foreground", Color.WHITE);
    }

    // ============ STUDENT PANEL ============
    private JPanel createStudentPanel() {
        JPanel panel = UIStyle.page();

        studentNameField = UIStyle.textField();
        studentEmailField = UIStyle.textField();
        studentIdField = UIStyle.textField();
        studentDeptField = UIStyle.textField();

        JButton addBtn = new RoundedButton("Add Student", RoundedButton.Style.PRIMARY);
        addBtn.addActionListener(e -> addStudent());
        JButton clearBtn = new RoundedButton("Clear Fields", RoundedButton.Style.SECONDARY);
        clearBtn.addActionListener(e -> clearStudentFields());

        JPanel form = UIStyle.labeledForm(
            "Add New Student",
            new String[]{"Name", "Email", "Student ID", "Department"},
            new JTextField[]{studentNameField, studentEmailField, studentIdField, studentDeptField},
            UIStyle.buttonRow(addBtn, clearBtn)
        );

        String[] columns = {"ID", "Name", "Email", "Student ID", "Department"};
        studentTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        studentTable = new JTable(studentTableModel);
        UIStyle.styleTable(studentTable);

        JButton deleteBtn = new RoundedButton("Delete Selected", RoundedButton.Style.DESTRUCTIVE);
        deleteBtn.addActionListener(e -> deleteStudent());
        JButton refreshBtn = new RoundedButton("Refresh", RoundedButton.Style.SECONDARY);
        refreshBtn.addActionListener(e -> refreshStudentTable());

        var listCard = UIStyle.cardWithScroll("Student List", studentTable);
        listCard.add(UIStyle.buttonRow(deleteBtn, refreshBtn), BorderLayout.SOUTH);

        panel.add(form, BorderLayout.NORTH);
        panel.add(listCard, BorderLayout.CENTER);

        refreshStudentTable();
        return panel;
    }

    // ============ ROOM PANEL ============
    private JPanel createRoomPanel() {
        JPanel panel = UIStyle.page();

        roomNumberField = UIStyle.textField();
        roomBuildingField = UIStyle.textField();
        roomCapacityField = UIStyle.textField();

        JButton addBtn = new RoundedButton("Add Room", RoundedButton.Style.PRIMARY);
        addBtn.addActionListener(e -> addRoom());
        JButton clearBtn = new RoundedButton("Clear Fields", RoundedButton.Style.SECONDARY);
        clearBtn.addActionListener(e -> clearRoomFields());

        JPanel form = UIStyle.labeledForm(
            "Add New Room",
            new String[]{"Room Number", "Building", "Capacity"},
            new JTextField[]{roomNumberField, roomBuildingField, roomCapacityField},
            UIStyle.buttonRow(addBtn, clearBtn)
        );

        String[] columns = {"Room #", "Building", "Capacity", "Available", "Status"};
        roomTableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        roomTable = new JTable(roomTableModel);
        UIStyle.styleTable(roomTable);

        JButton deleteBtn = new RoundedButton("Delete Selected", RoundedButton.Style.DESTRUCTIVE);
        deleteBtn.addActionListener(e -> deleteRoom());
        JButton refreshBtn = new RoundedButton("Refresh", RoundedButton.Style.SECONDARY);
        refreshBtn.addActionListener(e -> refreshRoomTable());

        var listCard = UIStyle.cardWithScroll("Room List", roomTable);
        listCard.add(UIStyle.buttonRow(deleteBtn, refreshBtn), BorderLayout.SOUTH);

        panel.add(form, BorderLayout.NORTH);
        panel.add(listCard, BorderLayout.CENTER);

        refreshRoomTable();
        return panel;
    }

    // ============ SCHEDULE PANEL ============
    private JPanel createSchedulePanel() {
        JPanel panel = UIStyle.page();

        scheduleCodeField = UIStyle.textField();
        scheduleNameField = UIStyle.textField();
        scheduleDurationField = UIStyle.textField();

        JButton createBtn = new RoundedButton("Create Schedule", RoundedButton.Style.PRIMARY);
        createBtn.addActionListener(e -> createSchedule());
        JButton enrollBtn = new RoundedButton("Enroll Student", RoundedButton.Style.SECONDARY);
        enrollBtn.addActionListener(e -> enrollStudent());

        JPanel form = UIStyle.labeledForm(
            "Create Exam Schedule",
            new String[]{"Course Code", "Course Name", "Duration (minutes)"},
            new JTextField[]{scheduleCodeField, scheduleNameField, scheduleDurationField},
            UIStyle.buttonRow(createBtn, enrollBtn)
        );

        scheduleDisplayArea = UIStyle.monospaceArea();

        JButton generateBtn = new RoundedButton("Generate All Seating", RoundedButton.Style.PRIMARY);
        generateBtn.addActionListener(e -> generateSeating());
        JButton refreshBtn = new RoundedButton("Refresh Schedules", RoundedButton.Style.SECONDARY);
        refreshBtn.addActionListener(e -> refreshScheduleDisplay());

        var displayCard = UIStyle.cardWithScroll("Exam Schedules", scheduleDisplayArea);
        displayCard.add(UIStyle.buttonRow(generateBtn, refreshBtn), BorderLayout.SOUTH);

        panel.add(form, BorderLayout.NORTH);
        panel.add(displayCard, BorderLayout.CENTER);

        refreshScheduleDisplay();
        return panel;
    }

    // ============ SEATING PANEL ============
    private JPanel createSeatingPanel() {
        JPanel panel = UIStyle.page();

        seatingDisplayArea = UIStyle.monospaceArea();

        JButton showBtn = new RoundedButton("Show Seating Charts", RoundedButton.Style.PRIMARY);
        showBtn.addActionListener(e -> displaySeatingCharts());
        JButton clearBtn = new RoundedButton("Clear Display", RoundedButton.Style.SECONDARY);
        clearBtn.addActionListener(e -> seatingDisplayArea.setText(""));

        var displayCard = UIStyle.cardWithScroll("Seating Charts", seatingDisplayArea);

        panel.add(UIStyle.buttonRow(showBtn, clearBtn), BorderLayout.NORTH);
        panel.add(displayCard, BorderLayout.CENTER);

        return panel;
    }

    // ============ REPORT PANEL ============
    private JPanel createReportPanel() {
        JPanel panel = UIStyle.page();

        JTextArea reportArea = UIStyle.monospaceArea();

        JButton generateBtn = new RoundedButton("Generate Report", RoundedButton.Style.PRIMARY);
        generateBtn.addActionListener(e -> reportArea.setText(service.getSummary()));
        JButton clearBtn = new RoundedButton("Clear", RoundedButton.Style.SECONDARY);
        clearBtn.addActionListener(e -> reportArea.setText(""));

        var displayCard = UIStyle.cardWithScroll("System Summary", reportArea);

        panel.add(UIStyle.buttonRow(generateBtn, clearBtn), BorderLayout.NORTH);
        panel.add(displayCard, BorderLayout.CENTER);

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
            sb.append("\n").append("=".repeat(50)).append("\n");
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
        SwingUtilities.invokeLater(MainFrame::new);
    }
}
