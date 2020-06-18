package com.hackzero.todoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    public Toolbar toolbar;
    public RecyclerView todoList;
    public Stack<String> tasksList;
    public Stack<Integer> tasksStatus, tasksPriorities;
    public DatabaseHelper databaseHelper;
    public SQLiteDatabase sqLiteDatabase;
    public TextView emptyView;

    public Cursor cursor;
    public int statusFlag = 0, priorityFlag = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //boiler plate code
        databaseHelper = new DatabaseHelper(this);
        sqLiteDatabase = databaseHelper.getWritableDatabase();
        tasksList = new Stack<>();
        tasksStatus = new Stack<>();
        tasksPriorities = new Stack<>();
        toolbar = findViewById(R.id.toolbar);
        todoList = findViewById(R.id.todo_list);
        emptyView = findViewById(R.id.empty_view);

        //to read the data from database
        cursor = sqLiteDatabase.rawQuery("SELECT TITLE, STATUS, PRIORITY FROM TASKS", new String[]{});
        if (cursor != null) {
            cursor.moveToFirst();
        }
        //traverse the table till the end
        if (cursor.moveToFirst()) {
            do {
                String task = cursor.getString(0);
                String taskStatus = cursor.getString(1);
                String taskPriority = cursor.getString(2);

                //check for the status of task
                if (taskStatus.equals("Done"))
                    statusFlag = 1;
                else
                    statusFlag = 0;

                //check for the priority of task
                if(taskPriority.equals("YES"))
                    priorityFlag = 1;
                else
                    priorityFlag = 0;

                //inserting data into required lists
                tasksList.push(task);
                tasksStatus.push(statusFlag);
                tasksPriorities.push(priorityFlag);

            } while (cursor.moveToNext());
            cursor.close();
        }


        //set the custom app bar
        setSupportActionBar(toolbar);

        //to make appropriate view visible
        if(tasksList.isEmpty()){
            todoList.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
            todoList.setVisibility(View.VISIBLE);
        }

        todoList.setLayoutManager(new LinearLayoutManager(this));
        todoList.setAdapter(new ListAdapter(tasksList, tasksStatus, tasksPriorities));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.add_icon == item.getItemId())
            showAddTaskDialog();
        else if (R.id.search_icon == item.getItemId()) {
            //TODO: implement search functionality
        }
        return super.onOptionsItemSelected(item);
    }

    //function to create and show custom dialog box to add task
    public void showAddTaskDialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        View addTaskView = getLayoutInflater().inflate(R.layout.addtask_dialog, null);

        final EditText addTaskEditText = addTaskView.findViewById(R.id.addTask_text);
        final Button addTaskButton = addTaskView.findViewById(R.id.addTask_btn);

        //setting the view of alert dialog (custom view that we created)
        alertDialog.setView(addTaskView);

        alertDialog.create();

        final AlertDialog dialog = alertDialog.create();
        dialog.setCanceledOnTouchOutside(true);

        dialog.show();


        addTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String task = addTaskEditText.getText().toString();
                if (!task.isEmpty()) {

                    //insert the data in database
                    databaseHelper.insertData(task, "Not Done", "NO", sqLiteDatabase);

                    //to make recycler view visible
                    emptyView.setVisibility(View.GONE);
                    todoList.setVisibility(View.VISIBLE);

                    tasksList.push(task);
                    tasksStatus.push(0);
                    tasksPriorities.push(0);
//                    todoList.setAdapter(new ListAdapter(tasksList, tasksStatus, tasksPriorities));
                    dialog.dismiss();
                } else
                    Toast.makeText(MainActivity.this, "Task field is empty", Toast.LENGTH_SHORT).show();

            }
        });
    }
}