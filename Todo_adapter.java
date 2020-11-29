package com.example.todo.Adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.recyclerview.widget.RecyclerView;

import com.example.todo.AddNewTask;
import com.example.todo.MainActivity;
import com.example.todo.Model.Todo_model;
import com.example.todo.R;
import com.example.todo.utils.dataHandaler;

import java.util.List;



public class Todo_adapter extends RecyclerView.Adapter<Todo_adapter.ViewHolder>
{
    private List<Todo_model> todoModelList;
    private MainActivity activity;
    private dataHandaler db;

    public Todo_adapter(dataHandaler db,MainActivity activity)
    {
        this.db=db;
        this.activity=activity;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.todo_layout, parent,false);
        return new ViewHolder(itemView);
    }
    public void onBindViewHolder(ViewHolder holder,int position){
        db.openDatabase();
        final Todo_model item=todoModelList.get(position);
        holder.Todo.setText(item.getTask());
        holder.Todo.setChecked(toBoolean(item.getStatus()));
        //check and uncheck task
        holder.Todo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    db.updateStatus(item.getId(),1);
                }
                else{
                    db.updateStatus(item.getId(),0);
                }
            }
        });
    }
    public int getItemCount(){
        return todoModelList.size();
    }
    private boolean toBoolean(int n){
        return n!=0;
    }
    public void setTask(List<Todo_model>todoModelList){
        this.todoModelList= todoModelList;
    notifyDataSetChanged();
    }
    public Context getContext(){
        return activity;
    }

    //delete function
    public void deleteitem(int position){
        Todo_model item=todoModelList.get(position);
        db.deleteTask(item.getId());
        todoModelList.remove(position);
        notifyItemRemoved(position);
    }

    //edit an item which is to update
    public void  editItem(int position){
        Todo_model item= todoModelList.get(position);
        Bundle bundle=new Bundle();
        bundle.putInt("id",item.getId());
        bundle.putString("task",item.getTask());
        AddNewTask fragment =new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(),AddNewTask.TAG);
    }
    public static class ViewHolder extends RecyclerView.ViewHolder{
        CheckBox Todo;
        ViewHolder(View view){
            super(view);
            Todo=view.findViewById(R.id.todoCheckbox);

        }
    }
}
