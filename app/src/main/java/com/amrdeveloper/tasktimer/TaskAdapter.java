package com.amrdeveloper.tasktimer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class TaskAdapter extends ListAdapter<Task, TaskAdapter.TaskViewHolder> {

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    @FunctionalInterface
    public interface OnTaskClickListener {
        void onTaskClicked(Task task);
    }

    private OnTaskClickListener mOnTaskClickListener;

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
        Task currentTask = getItem(position);
        if (currentTask != null) {
            holder.bindTask(currentTask);
        }
    }

    public void notifyRunningItems() {
        List<Task> currentList = getCurrentList();
        int size = currentList.size();
        for (int i = 0; i < size; i++) {
            if (currentList.get(i).isRunning()) {
                notifyItemChanged(i);
            }
        }
    }

    public Task getTaskAt(int position) {
        return getItem(position);
    }

    public void setOnTaskClickListener(OnTaskClickListener listener) {
        mOnTaskClickListener = listener;
    }

    private static final DiffUtil.ItemCallback<Task> DIFF_CALLBACK = new DiffUtil.ItemCallback<Task>() {
        @Override
        public boolean areItemsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Task oldItem, @NonNull Task newItem) {
            return oldItem.getTitle().equals(newItem.getTitle());
        }
    };

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTaskTitle;
        private TextView mTaskTimer;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            initViews(itemView);
            itemView.setOnClickListener(this);
        }

        private void initViews(View view) {
            mTaskTitle = view.findViewById(R.id.taskTitle);
            mTaskTimer = view.findViewById(R.id.taskTimer);
        }

        private void bindTask(Task task) {
            mTaskTitle.setText(task.getTitle());
            mTaskTimer.setText(TimeUtils.formatSecondsToTime(task.getTimeInSec()));
        }

        @Override
        public void onClick(View view) {
            if (mOnTaskClickListener != null) {
                int clickedPosition = getAdapterPosition();
                mOnTaskClickListener.onTaskClicked(getItem(clickedPosition));
            }
        }
    }
}
