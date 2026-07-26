package examsystem.model;

import java.util.ArrayList;
import java.util.List;

public class ExamRoom {
    private int roomNumber;
    private String building;
    private int capacity;
    private int totalRows;
    private int totalColumns;
    private String[][] seatingGrid;
    private List<Student> assignedStudents;
    
    public ExamRoom(int roomNumber, String building, int capacity) {
        this.roomNumber = roomNumber;
        this.building = building;
        this.capacity = capacity;
        this.assignedStudents = new ArrayList<>();
        calculateGridDimensions();
        initializeSeatingGrid();
    }
    
    private void calculateGridDimensions() {
        this.totalRows = (int) Math.ceil(Math.sqrt(capacity));
        this.totalColumns = (int) Math.ceil((double) capacity / totalRows);
    }
    
    private void initializeSeatingGrid() {
        seatingGrid = new String[totalRows][totalColumns];
    }
    
    public boolean assignStudent(Student student) {
        if (assignedStudents.size() >= capacity) {
            return false;
        }
        
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalColumns; col++) {
                if (seatingGrid[row][col] == null) {
                    seatingGrid[row][col] = student.getStudentId();
                    assignedStudents.add(student);
                    return true;
                }
            }
        }
        return false;
    }
    
    public void removeStudent(String studentId) {
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalColumns; col++) {
                if (studentId.equals(seatingGrid[row][col])) {
                    seatingGrid[row][col] = null;
                    assignedStudents.removeIf(s -> s.getStudentId().equals(studentId));
                    return;
                }
            }
        }
    }
    
    public void clearSeating() {
        assignedStudents.clear();
        initializeSeatingGrid();
    }
    
    public String getSeatingChartAsString() {
        StringBuilder sb = new StringBuilder();
        sb.append("\nRoom ").append(roomNumber).append(" | ").append(building);
        sb.append(" | Capacity: ").append(capacity);
        sb.append(" | Available: ").append(getAvailableSeats()).append("\n");
        sb.append("-".repeat(40)).append("\n");
        
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalColumns; col++) {
                if (seatingGrid[row][col] != null) {
                    sb.append(" [S] ");
                } else {
                    sb.append(" [ ] ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }
    
    // Getters
    public int getRoomNumber() { return roomNumber; }
    public String getBuilding() { return building; }
    public int getCapacity() { return capacity; }
    public int getTotalRows() { return totalRows; }
    public int getTotalColumns() { return totalColumns; }
    public String[][] getSeatingGrid() { return seatingGrid; }
    public List<Student> getAssignedStudents() { return assignedStudents; }
    public int getAvailableSeats() { return capacity - assignedStudents.size(); }
    public boolean isFull() { return assignedStudents.size() >= capacity; }
    
    @Override
    public String toString() {
        return String.format("Room %d | %s | Capacity: %d | Available: %d", 
                            roomNumber, building, capacity, getAvailableSeats());
    }
}