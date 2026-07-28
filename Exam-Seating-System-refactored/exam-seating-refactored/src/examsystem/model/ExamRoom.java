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
        int[] emptySeat = findSeat(seat -> seatingGrid[seat[0]][seat[1]] == null);
        if (emptySeat == null) {
            return false;
        }
        seatingGrid[emptySeat[0]][emptySeat[1]] = student.getStudentId();
        assignedStudents.add(student);
        return true;
    }
    
    public void removeStudent(String studentId) {
        int[] seat = findSeat(s -> studentId.equals(seatingGrid[s[0]][s[1]]));
        if (seat != null) {
            seatingGrid[seat[0]][seat[1]] = null;
            assignedStudents.removeIf(s -> s.getStudentId().equals(studentId));
        }
    }
    
    /** Scans the grid in row-major order and returns the first {row, col} matching the predicate, or null. */
    private int[] findSeat(java.util.function.Predicate<int[]> matches) {
        for (int row = 0; row < totalRows; row++) {
            for (int col = 0; col < totalColumns; col++) {
                int[] seat = {row, col};
                if (matches.test(seat)) {
                    return seat;
                }
            }
        }
        return null;
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