package com.example.countryquizapp.UI_and_Helpers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.countryquizapp.Database.DatabaseManager;
import com.example.countryquizapp.Model.Attempt;
import com.example.countryquizapp.Model.myApp;
import com.example.countryquizapp.R;

import java.util.ArrayList;
import java.util.List;

public class AttemptsReportActivity extends AppCompatActivity implements DatabaseManager.DatabaseListener{//,AttemptReportAdapter.ListClickListener

    ArrayList<Attempt> attemptListFromDB = new ArrayList<>(0) ;
    //DatabaseManager dbManager = new DatabaseManager();
    DatabaseManager dbManager;
    AttemptReportAdapter adapter;
    AlertDialog.Builder builder;
    ListView listOfAttempts;
    TextView numOfAttempts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attempts_report);

        builder = new AlertDialog.Builder(this);
        //Intent fromMain = getIntent();
        dbManager = ((myApp)getApplication()).getdbManager();

        listOfAttempts = (ListView) findViewById(R.id.list_of_attempts);
        numOfAttempts = (TextView) findViewById(R.id.num_of_attempts);
        dbManager.listener = this;

        if(!(this.getIntent().getParcelableArrayListExtra("All attempts")==null)){
            attemptListFromDB = this.getIntent().getParcelableArrayListExtra("All attempts");
            dbManager.getAllAttempts();
            System.out.println("My saved attempts is: \n"+ attemptListFromDB.toString());}
        else {
            dbManager.getAllAttempts();
            //attemptListFromDB = this.getIntent().getParcelableArrayListExtra("All attempts");
            System.out.println("My saved attempts is: \n"+ attemptListFromDB);}


       listOfAttempts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                dbManager.deleteAttempt(attemptListFromDB.get(position));
                dbManager.getAllAttempts();
                adapter.listOfAttempts.remove(position);
                // we have to remove it from db as well
                Toast.makeText(getApplicationContext(),"Attempt deleted",Toast.LENGTH_SHORT).show();
                adapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this,"QuizReport started",Toast.LENGTH_SHORT).show();
        dbManager = ((myApp)getApplication()).getdbManager();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this,"QuizReport restarted",Toast.LENGTH_SHORT).show();
        dbManager = ((myApp)getApplication()).getdbManager();
    }

    protected void onResume() {
        super.onResume();
        dbManager = ((myApp)getApplication()).getdbManager();
    }

    @Override
    public void databaseAllAttemptsListener(List<Attempt> list) {
        attemptListFromDB = new ArrayList<>(list);
        System.out.println("This is my db list:"+ attemptListFromDB);
        adapter= new AttemptReportAdapter(attemptListFromDB, this);
        listOfAttempts.setAdapter(adapter);
        numOfAttempts.setText("The number of attempts is " + attemptListFromDB.size());
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList("All attempts", attemptListFromDB);
    }



    public void onClick(View view) {
    showDeleteOption();
    }

    private void showDeleteOption(){
        builder.setTitle(getResources().getString(R.string.delete_text));
        builder.setMessage("Use this icon only if you want to delete all attempts"+
                "\nTo delete one attempt at a time, click on the item in the list below and it will be erased");
        builder.setPositiveButton(getResources().getString(R.string.delAll), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dbManager.deleteAll();
                dbManager.getAllAttempts();
                adapter.notifyDataSetChanged();
                Toast.makeText(getApplicationContext(),"All attempts were delete",Toast.LENGTH_SHORT).show();

            }
        });

        builder.setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"No change was made to your list of attempts",Toast.LENGTH_SHORT).show();

            }
        });
        AlertDialog alertDialog=builder.create();
        builder.show();

    }
}