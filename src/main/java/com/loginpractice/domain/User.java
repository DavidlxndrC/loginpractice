package com.loginpractice.domain;

public class User {

    private int id;
    private String userName;
    private String fullName;
    private Role role;
    private Status status;
    private String passwordHash;

    public User( String userName, String fullName, Role role, Status status, String passwordHash) {

        this.userName = userName;
        this.fullName = fullName;
        this.role = role;
        this.status = status;
        this.passwordHash = passwordHash;
    }

    public int getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getFullName() {
        return fullName;
    }

    public Role getRole() {
        return role;
    }

    public Status getStatus() {
        return status;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
}



