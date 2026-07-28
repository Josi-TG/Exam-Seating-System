package examsystem.model;

public abstract class Person {
    protected int id;
    protected String name;
    protected String email;
    private static int nextId = 1;
    
    public Person(String name, String email) {
        this.id = nextId++;
        this.name = name;
        this.email = email;
    }
    
    public abstract void display();
    
    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Email: %s", id, name, email);
    }
}