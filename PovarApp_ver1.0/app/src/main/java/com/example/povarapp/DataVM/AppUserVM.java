package com.example.povarapp.DataVM;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class AppUserVM implements Serializable {
    private int id;
    private String name;
    private String password;
    private String email;
    private Date date;

    public AppUserVM() {}

    public void SetId(int id) { this.id = id; }
    public int GetId() {
        return this.id;
    }

    public void SetName(String name) {
        this.name = name;
    }
    public String GetName() {
        return this.name;
    }

    public void SetPassword(String password) {
        this.password = password;
    }
    public String GetPassword() {
        return this.password;
    }

    public String GetEmail() {
        return email;
    }
    public void SetEmail(String email) {
        this.email = email;
    }

    public Date GetDate() {
        return date;
    }
    public void SetDate(Date date) {
        this.date = date;
    }
}
