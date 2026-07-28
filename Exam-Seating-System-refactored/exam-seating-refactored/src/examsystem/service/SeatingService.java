package examsystem.service;

import examsystem.model.*;
import java.time.LocalDateTime;
import java.util.*;

public class SeatingService {
    private List<Student> students;
    private List<ExamRoom> rooms;
    private List<ExamSchedule> schedules;
    
    public SeatingService() {
        this.students = new ArrayList<>();
        this.rooms = new ArrayList<>();
        this.schedules = new ArrayList<>();
    }
    
    // ============ STUDENT OPERATIONS ============
    public void addStudent(Student student) {
        students.add(student);
    }
    
    public void addStudent(String name, String email, String studentId, String department) {
        students.add(new Student(name, email, studentId, department));
    }
    
    public List<Student> getAllStudents() {
        return new ArrayList<>(students);
    }
    
    public Student findStudentById(String studentId) {
        for (Student s : students) {
            if (s.getStudentId().equals(studentId)) {
                return s;
            }
        }
        return null;
    }
    
    public Student findStudentByName(String name) {
        for (Student s : students) {
            if (s.getName().equalsIgnoreCase(name)) {
                return s;
            }
        }
        return null;
    }
    
    public boolean updateStudent(String studentId, String newName, String newEmail, String newDepartment) {
        Student student = findStudentById(studentId);
        if (student != null) {
            student.setName(newName);
            student.setEmail(newEmail);
            student.setDepartment(newDepartment);
            return true;
        }
        return false;
    }
    
    public boolean deleteStudent(String studentId) {
        Student toRemove = findStudentById(studentId);
        if (toRemove != null) {
            students.remove(toRemove);
            return true;
        }
        return false;
    }
    
    // ============ ROOM OPERATIONS ============
    public void addRoom(ExamRoom room) {
        rooms.add(room);
    }
    
    public void addRoom(int roomNumber, String building, int capacity) {
        rooms.add(new ExamRoom(roomNumber, building, capacity));
    }
    
    public List<ExamRoom> getAllRooms() {
        return new ArrayList<>(rooms);
    }
    
    public ExamRoom findRoom(int roomNumber) {
        for (ExamRoom room : rooms) {
            if (room.getRoomNumber() == roomNumber) {
                return room;
            }
        }
        return null;
    }
    
    public ExamRoom findAvailableRoom(int requiredCapacity) {
        for (ExamRoom room : rooms) {
            if (room.getAvailableSeats() >= requiredCapacity) {
                return room;
            }
        }
        return null;
    }
    
    public boolean deleteRoom(int roomNumber) {
        ExamRoom toRemove = findRoom(roomNumber);
        if (toRemove != null) {
            rooms.remove(toRemove);
            return true;
        }
        return false;
    }
    
    // ============ SCHEDULE OPERATIONS ============
    public void addSchedule(ExamSchedule schedule) {
        schedules.add(schedule);
    }
    
    public void createSchedule(String courseCode, String courseName, LocalDateTime dateTime, int duration) {
        schedules.add(new ExamSchedule(courseCode, courseName, dateTime, duration));
    }
    
    public List<ExamSchedule> getAllSchedules() {
        return new ArrayList<>(schedules);
    }
    
    public ExamSchedule findSchedule(String courseCode) {
        for (ExamSchedule s : schedules) {
            if (s.getCourseCode().equals(courseCode)) {
                return s;
            }
        }
        return null;
    }
    
    public boolean enrollStudentInExam(String studentId, String courseCode) {
        Student student = findStudentById(studentId);
        ExamSchedule schedule = findSchedule(courseCode);
        
        if (student != null && schedule != null) {
            schedule.addStudent(student);
            return true;
        }
        return false;
    }
    
    public boolean removeStudentFromExam(String studentId, String courseCode) {
        Student student = findStudentById(studentId);
        ExamSchedule schedule = findSchedule(courseCode);
        
        if (student != null && schedule != null) {
            schedule.removeStudent(student);
            return true;
        }
        return false;
    }
    
    // ============ SEATING GENERATION ============
    public void generateSeatingForAllExams() {
        for (ExamSchedule schedule : schedules) {
            if (schedule.getStudentCount() > 0) {
                ExamRoom room = findAvailableRoom(schedule.getStudentCount());
                if (room != null) {
                    schedule.assignRoom(room);
                }
            }
        }
    }
    
    public void generateSeatingForExam(String courseCode) {
        ExamSchedule schedule = findSchedule(courseCode);
        if (schedule != null && schedule.getStudentCount() > 0) {
            ExamRoom room = findAvailableRoom(schedule.getStudentCount());
            if (room != null) {
                schedule.assignRoom(room);
            }
        }
    }
    
    // ============ STATISTICS ============
    public int getTotalStudents() { return students.size(); }
    public int getTotalRooms() { return rooms.size(); }
    public int getTotalSchedules() { return schedules.size(); }
    public int getTotalAssignedStudents() {
        int total = 0;
        for (ExamSchedule s : schedules) {
            total += s.getStudentCount();
        }
        return total;
    }
    
    public String getSummary() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== SYSTEM SUMMARY ===\n");
        sb.append("Total Students: ").append(getTotalStudents()).append("\n");
        sb.append("Total Rooms: ").append(getTotalRooms()).append("\n");
        sb.append("Total Schedules: ").append(getTotalSchedules()).append("\n");
        sb.append("Total Enrollments: ").append(getTotalAssignedStudents()).append("\n");
        return sb.toString();
    }
}