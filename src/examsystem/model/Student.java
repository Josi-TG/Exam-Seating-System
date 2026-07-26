package examsystem.model;

public class Student extends Person {
    private String studentId;
    private String department;
    private int year;
    
    public Student(String name, String email, String studentId, String department) {
        super(name, email);
        this.studentId = studentId;
        this.department = department;
        this.year = 1;
    }
    
    public Student(String name, String email, String phone, String studentId, String department) {
        super(name, email);
        this.studentId = studentId;
        this.department = department;
        this.year = 1;
    }
    
    @Override
    public void display() {
        System.out.println("Student: " + name + " (ID: " + studentId + ")");
        System.out.println("  Department: " + department);
        System.out.println("  Year: " + year);
    }
    
    // Getters and Setters
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }
    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }
    
    @Override
    public String toString() {
        return String.format("%s | %s | %s", studentId, name, department);
    }
}