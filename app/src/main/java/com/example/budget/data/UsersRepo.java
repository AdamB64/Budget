package com.example.budget.data;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * create the methods to use the query's and inserts from the DAO
 */
public class UsersRepo {

    private UsersDAO mUsersDAO;

    public UsersRepo(Context context){
        super();
        mUsersDAO=UsersDatabase.getInstance(context).UsersDAO();
    }

    public void addUser(Users user){
        this.mUsersDAO.insertOrUpdateUser(user);
    }

    public List<Users> GetIncome(String username){
        return this.mUsersDAO.FindIncome(username);
    }

    public List<Users> GetExpenses(String username){
        return this.mUsersDAO.FindExpenses(username);
    }

    public List<Users> findusers(String username,String password){
        return this.mUsersDAO.FindUser(username,password);
    }

    public List<Users> findGoal(String username){
        return this.mUsersDAO.findGoal(username);
    }

    public List<Users> findBudget(String username){
        return this.mUsersDAO.findBudget(username);
    }
}
