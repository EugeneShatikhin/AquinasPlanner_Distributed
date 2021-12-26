package com.aquinas.serviceusers.repository.model;

import javax.persistence.*;

@Entity
@Table(name = "users")
public final class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String username;
    private String password;
    private String firstname;
    private String lastname;
    private String bio;

    public User() {
    }

    public User(String username, String password, String firstname, String lastname, String bio) {
        this.username = username;
        this.password = password;
        this.firstname = firstname;
        this.lastname = lastname;
        this.bio = bio;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

}
