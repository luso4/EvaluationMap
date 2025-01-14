package org.example;

public class User {
    private String email;
    private String name;
    private int director;
    private String department;

    // Default constructor
    public User() {
    }

    // Getters and Setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDirector() {
        return director;
    }

    public void setDirector(int director) {
        this.director = director;
    }

    public String getDepartment() {return department;}

    public void setDepartment(String department) {this.department = department;}

}
