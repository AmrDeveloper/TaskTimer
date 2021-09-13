package com.amrdeveloper.tasktimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Objects;

public class AddTaskActivity extends AppCompatActivity {

    private EditText editTaskTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        initLayoutViews();

        Objects.requireNonNull(getSupportActionBar()).setHomeAsUpIndicator(R.drawable.ic_close_vector);
    }

    private void initLayoutViews() {
        editTaskTitle = findViewById(R.id.editTaskTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_task_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveTask) {
            saveNewTask();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveNewTask() {
        String taskTitle = editTaskTitle.getText().toString();
        if (taskTitle.isEmpty()) {
            Toast.makeText(this, "You must add title for Task", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(Constant.EXTRA_TITLE, taskTitle);
        setResult(RESULT_OK, data);
        finish();
    }
}
