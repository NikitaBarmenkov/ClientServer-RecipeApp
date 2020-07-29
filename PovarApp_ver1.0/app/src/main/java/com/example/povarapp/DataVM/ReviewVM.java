package com.example.povarapp.DataVM;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Date;

public class ReviewVM implements Serializable {
    private int id;
    private double rating;
    @SerializedName("UserId")
    private int userId;
    private String userName;
    @SerializedName("DishId")
    private int dishId;
    private String comment;
    private Date date;

    public ReviewVM() {}

    public int GetId() {
        return id;
    }
    public void SetId(int id) {
        this.id = id;
    }

    public double GetRating() {
        return rating;
    }
    public void SetRating(double rating) {
        this.rating = rating;
    }

    public int GetUserId() {
        return userId;
    }
    public void SetUserId(int userId) {
        this.userId = userId;
    }

    public int GetDishId() {
        return dishId;
    }
    public void SetDishId(int dishId) {
        this.dishId = dishId;
    }

    public String GetComment() {
        return comment;
    }
    public void SetComment(String comment) {
        this.comment = comment;
    }

    public Date GetDate() {
        return date;
    }
    public void SetDate(Date date) {
        this.date = date;
    }

    public String GetUserName() {
        return userName;
    }

    public void SetUserName(String userName) {
        this.userName = userName;
    }
}
