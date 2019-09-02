package com.amrdeveloper.tasktimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder>{

    private List<Task> mTasksList;

    @FunctionalInterface
    public interface OnTaskClickListener{
        void onTaskClicked(Task task);
    }

    private OnTaskClickListener mOnTaskClickListener;

    public TaskAdapter(){
        mTasksList = new ArrayList<>();
    }

    public TaskAdapter(List<Task> taskList){
        mTasksList = taskList;
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final Context context = parent.getContext();
        int layoutID = R.layout.task_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        final boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(layoutID, parent, shouldAttachToParentImmediately);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
         Task currentTask = mTasksList.get(position);
         if(currentTask != null){
             holder.bindTask(currentTask);
         }
    }

    @Override
    public int getItemCount() {
        return mTasksList.size();
    }

    public void updateAdapterData(List<Task> taskList){
        if(taskList != null){
            this.mTasksList = taskList;
            notifyDataSetChanged();
        }
    }

    public Task getTaskAt(int position){
        return mTasksList.get(position);
    }

    public void notifyRunningItems(){
        for(int i = 0 ; i < mTasksList.size() ; i++)
            if(mTasksList.get(i).isRunning())
                notifyItemChanged(i);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener){
        mOnTaskClickListener = listener;
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private TextView mTaskTitle;
        private TextView mTaskTimer;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            itemView.setOnClickListener(this);
        }

        private void initViews(View view){
            mTaskTitle = view.findViewById(R.id.taskTitle);
            mTaskTimer = view.findViewById(R.id.taskTimer);
        }

        private void bindTask(Task task){
            mTaskTitle.setText(task.getTitle());
            mTaskTimer.setText(TimeUtils.formatSecondsToTime(task.getTimeInSec()));
        }

        @Override
        public void onClick(View view) {
           if(mOnTaskClickListener != null){
               int clickedPosition = getAdapterPosition();
               mOnTaskClickListener.onTaskClicked(mTasksList.get(clickedPosition));
           }
        }
    }
}
