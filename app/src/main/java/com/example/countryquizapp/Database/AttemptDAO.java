package com.example.countryquizapp.Database;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.countryquizapp.Model.Attempt;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface AttemptDAO {
    @Insert
    void insertAttempt(Attempt attempt);

   @Delete
   void deleteAttempt(Attempt attemptToDelete);

    @Query("DELETE FROM Attempt")
    void deleteAll();

    @Query("SELECT * FROM Attempt")
    List<Attempt> getAll();


}
