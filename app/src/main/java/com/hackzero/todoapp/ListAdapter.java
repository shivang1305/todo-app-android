package com.hackzero.todoapp;

import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Stack;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListViewHolder> {

    private Stack<String> taskText;
    private Stack<Integer> taskStatus;
    private Stack<Integer> taskPriority;

    //constructor
    public ListAdapter(Stack<String> data, Stack<Integer> status, Stack<Integer> priority) {
        this.taskText = data;
        this.taskStatus = status;
        this.taskPriority = priority;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_view, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, final int position) {

        final UserInterface userInterface = new UserInterface();

        final String title = taskText.get(position);
        holder.taskTitle.setText(title);

        if(taskStatus.get(position) == 1){
            //mark the task as done
            userInterface.taskDoneUI(holder);
        }

        if(taskPriority.get(position) == 1 && taskStatus.get(position) == 0){
            //change the UI of particular task
            userInterface.priorityTaskUI(holder);
        }


        //when user click done button
        holder.taskDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //database access
                DatabaseHelper helper = new DatabaseHelper(view.getContext());
                SQLiteDatabase database = helper.getWritableDatabase();

                //marks the task as done
                if (taskStatus.get(position) == 0) {
                    //update the database
                    helper.updateData(title, "STATUS", "Done", database);

                    taskStatus.set(position, 1);
                    taskPriority.set(position, 0);

                    //change the UI accordingly
                    userInterface.taskDoneUI(holder);

                    //change the UI of particular task
                    userInterface.setWhiteBackgroundUI(holder);
                }

                //deletes the task from the list
                else if(taskStatus.get(position) == 1) {
                    //when the user clicks on delete button
                    holder.listLayout.removeView(holder.taskView);

                    //deletion from database
                    helper.deleteData(title, database);
                }
            }
        });

        //when the user click return button
        holder.returnBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //database access
                DatabaseHelper helper = new DatabaseHelper(view.getContext());
                SQLiteDatabase database = helper.getWritableDatabase();

                //when the task is marked as done
                if(taskStatus.get(position) == 1){
                    //update the database
                    helper.updateData(title, "STATUS", "Not Done", database);
                    taskStatus.set(position, 0);

                    //change the UI of that task accordingly
                    userInterface.normalTaskUI(holder);
                }

                //when the user click on flag button
                else{
                    //get the background color of particular task
                    ColorDrawable cd = (ColorDrawable) holder.listView.getBackground();

                    //task is not set to priority
                    if(cd.getColor() == Color.parseColor("#FFFFFF")){
                        //update the database
                        helper.updateData(title, "PRIORITY", "YES", database);
                        taskPriority.set(position, 1);

                        //change the UI of particular task
                        userInterface.priorityTaskUI(holder);
                    }

                    //task is already set to priority
                    else if(cd.getColor() == Color.parseColor("#F9DDA4")){
                        //update the database (de-prioritize the task)
                        helper.updateData(title, "PRIORITY", "NO", database);
                        taskPriority.set(position, 0);

                        //change the UI of particular task
                        userInterface.setWhiteBackgroundUI(holder);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return taskText.size();
    }

    public class ListViewHolder extends RecyclerView.ViewHolder {
        TextView taskTitle;
        ImageButton taskDone, returnBtn;
        LinearLayout listLayout, taskView, listView;

        public ListViewHolder(@NonNull View itemView) {
            super(itemView);

            taskTitle = itemView.findViewById(R.id.taskTitle);
            taskDone = itemView.findViewById(R.id.done_btn);
            taskView = itemView.findViewById(R.id.listView);
            listView = itemView.findViewById(R.id.listView2);
            listLayout = itemView.findViewById(R.id.listLayout);
            returnBtn = itemView.findViewById(R.id.return_btn);
        }
    }
}
