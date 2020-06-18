package com.hackzero.todoapp;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import androidx.core.content.res.ResourcesCompat;

class UserInterface {
    public void taskDoneUI(final ListAdapter.ListViewHolder holder) {
        holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.taskDone.setImageResource(R.drawable.ic_baseline_delete);
        holder.returnBtn.setImageResource(R.drawable.ic_baseline_reply);
    }

    public void normalTaskUI(final ListAdapter.ListViewHolder holder) {
        holder.returnBtn.setImageResource(R.drawable.ic_baseline_outlined_flag);
        holder.taskDone.setImageResource(R.drawable.ic_baseline_done);
        holder.taskTitle.setPaintFlags(holder.taskTitle.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
    }

    public void priorityTaskUI(final ListAdapter.ListViewHolder holder) {
        holder.listView.setBackgroundColor(Color.parseColor("#F9DDA4"));
        holder.returnBtn.setBackgroundColor(Color.parseColor("#F9DDA4"));
        holder.taskDone.setBackgroundColor(Color.parseColor("#F9DDA4"));
        holder.taskTitle.setTypeface(holder.taskTitle.getTypeface(), Typeface.BOLD_ITALIC);
    }

    public void setWhiteBackgroundUI(final ListAdapter.ListViewHolder holder) {
        holder.listView.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.returnBtn.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.taskDone.setBackgroundColor(Color.parseColor("#FFFFFF"));
        holder.taskTitle.setTypeface(holder.taskTitle.getTypeface(), Typeface.ITALIC);
    }
}
