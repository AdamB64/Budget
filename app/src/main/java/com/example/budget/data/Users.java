package com.example.budget.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.security.PrivateKey;

@Entity(tableName="Users")
public class Users {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int uid;

    private String Password;

    private String UserName;
}
