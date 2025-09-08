package com.example.authservice.model;

public class User {
    private String id;
    private String username;
    private String passwordHash;
    private String role;
    private String email;

    public User() {}
    public User(String id, String username, String passwordHash, String role, String email) {
        this.id = id; this.username = username; this.passwordHash = passwordHash; this.role = role; this.email = email;
    }
    // getters and setters omitted for brevity (generate using IDE or Lombok)
    // ...
    public String getId(){return id;}
    public void setId(String id){this.id=id;}
    public String getUsername(){return username;}
    public void setUsername(String u){this.username=u;}
    public String getPasswordHash(){return passwordHash;}
    public void setPasswordHash(String p){this.passwordHash=p;}
    public String getRole(){return role;}
    public void setRole(String r){this.role=r;}
    public String getEmail(){return email;}
    public void setEmail(String e){this.email=e;}
}
