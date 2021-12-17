package com.example.countryquizapp.Database;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.room.Room;

import com.example.countryquizapp.Model.Attempt;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DatabaseManager {
    List<Attempt> list;
    static AttemptDatabase db;
    ExecutorService databaseExecutor = Executors.newFixedThreadPool(4);
    Handler db_handler = new Handler(Looper.getMainLooper());

    public interface DatabaseListener {
        void databaseAllAttemptsListener(List<Attempt> list);}

    public DatabaseListener listener;


    private static void BuildDBInstance (Context context) {
        db = Room.databaseBuilder(context,AttemptDatabase.class,"attempt_db").build();
    }
    public static AttemptDatabase getDBInstance(Context context){
        if (db == null){
            BuildDBInstance(context);
        }
        return db;
    }

    public void insertNewAttempt(Attempt attempt){
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.getAttemptDAO().insertAttempt(attempt);
            }
        });
    }

    public void getAllAttempts(){
                databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                list =  db.getAttemptDAO().getAll();
               // List<Attempt> list =  db.getAttemptDAO().getAll();
                db_handler.post(new Runnable() {
                    @Override
                    public void run() {

                        listener.databaseAllAttemptsListener(list);
                    }
                });

            }
        });
    }


    public void deleteAttempt(Attempt attempt){
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.getAttemptDAO().deleteAttempt(attempt);
            }
        });
    }

    public void deleteAll(){
        databaseExecutor.execute(new Runnable() {
            @Override
            public void run() {
                db.getAttemptDAO().deleteAll();
            }
        });
    }
}
