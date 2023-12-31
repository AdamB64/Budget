package com.example.budget.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


/**
 * the inserts and querys to inserts data and find data to/from the Users database
 */
@Dao
public interface UsersDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertOrUpdateUser(Users user);

    @Query("SELECT * FROM Users WHERE UserName LIKE :username AND Incamount>0")
    public List<Users> FindIncome(String username);

    @Query("SELECT * FROM Users WHERE UserName LIKE :username AND ExpAmount>0")
    public List<Users> FindExpenses(String username);

    @Query("SELECT * FROM Users Where UserName like :username and Password like :password")
    public List<Users> FindUser(String username,String password);

    @Query("SELECT * FROM Users WHERE UserName LIKE :username ORDER BY Goal DESC LIMIT 1")
    public List<Users> findGoal(String username);

    @Query("SELECT * FROM Users WHERE UserName LIKE :username AND Incamount>0 or ExpAmount>0")
    public List<Users> findBudget(String username);
}
