package examsystem.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ExamSchedule {
    private String courseCode;
    private String courseName;
    private LocalDateTime dateTime;
    private int duration;
    private List<Student> enrolledStudents;
    private ExamRoom assignedRoom;
    
    public ExamSchedule(String courseCode, String courseName, LocalDateTime dateTime, int duration) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.dateTime = dateTime;
        this.duration = duration;
        this.enrolledStudents = new ArrayList<>();
        this.assignedRoom = null;
    }
    
    public void addStudent(Student student) {
        if (!enrolledStudents.contains(student)) {
            enrolledStudents.add(student);
        }
    }
    
    public void removeStudent(Student student) {
        enrolledStudents.remove(student);
    }
    
    public void assignRoom(ExamRoom room) {
        this.assignedRoom = room;
        room.clearSeating();
        for (Student student : enrolledStudents) {
            room.assignStudent(student);
        }
    }
    
    public String getScheduleInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("Course: ").append(courseCode).append(" - ").append(courseName).append("\n");
        sb.append("Date/Time: ").append(dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"))).append("\n");
        sb.append("Duration: ").append(duration).append(" minutes\n");
        sb.append("Students Enrolled: ").append(enrolledStudents.size()).append("\n");
        
        if (assignedRoom != null) {
            sb.append("Assigned Room: ").append(assignedRoom.getRoomNumber());
            sb.append(" | Available Seats: ").append(assignedRoom.getAvailableSeats()).append("\n");
            sb.append(assignedRoom.getSeatingChartAsString());
        } else {
            sb.append("No room assigned yet!\n");
        }
        return sb.toString();
    }
    
    // Getters
    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public LocalDateTime getDateTime() { return dateTime; }
    public int getDuration() { return duration; }
    public List<Student> getEnrolledStudents() { return enrolledStudents; }
    public ExamRoom getAssignedRoom() { return assignedRoom; }
    public int getStudentCount() { return enrolledStudents.size(); }
    
    @Override
    public String toString() {
        return String.format("%s | %s | %d students | Room: %s", 
                            courseCode, courseName, enrolledStudents.size(), 
                            assignedRoom != null ? String.valueOf(assignedRoom.getRoomNumber()) : "None");
    }
}