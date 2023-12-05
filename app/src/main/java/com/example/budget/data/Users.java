package com.example.budget.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.PrivateKey;

/**
 * declare the values of the database and the getters and setters for the values
 */
@Entity(tableName="Users")
public class Users {



    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;




    private int Incamount;



    private int ExpAmount;


    private String date;



    private String description;



    private String Password;


    private String UserName;



    private int Goal;



    private int TotalBudget;

    public Users(){
    }
    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getIncamount() {
        return Incamount;
    }

    public void setIncamount(int incamount) {
        Incamount = incamount;
    }

    public int getExpAmount() {
        return ExpAmount;
    }

    public void setExpAmount(int expAmount) {
        ExpAmount = expAmount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public int getGoal() {
        return Goal;
    }

    public void setGoal(int goal) {
        Goal = goal;
    }

    public int getTotalBudget() {
        return TotalBudget;
    }

    public void setTotalBudget(int totalBudget) {
        TotalBudget = totalBudget;
    }

    @Override
    public String toString() {
        return "Users{" +
                "uid=" + uid +
                ", income amount=" + Incamount +
                ", expenses amount"+ExpAmount+
                ", date='" + date + '\'' +
                ", description='" + description + '\'' +
                ", Password='" + Password + '\'' +
                ", UserName='" + UserName + '\'' +
                ", Goal=" + Goal +
                ", TotalBudget=" + TotalBudget +
                '}';
    }
}