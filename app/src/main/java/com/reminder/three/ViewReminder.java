package com.reminder.three;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.sql.SQLException;

public class ViewReminder extends AppCompatActivity {

    private long id;
    private Tasks tasks;
    private TasksDataSource datasource;
    private TextView txtTask;
    private TextView txtDate;
    private TextView txtTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTask = (TextView) findViewById(R.id.txtViewTask);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);

        Intent getIntent = getIntent();
        id = getIntent.getLongExtra("taskID", -1);

        if(id != -1) {
            datasource = new TasksDataSource(this);
            try {
                datasource.open();
                tasks = datasource.retrieveTask(id);
                txtTask.setText(tasks.getTask());
                txtDate.setText(tasks.getDateTime().getDate() + "/" + tasks.getDateTime().getMonth() + "/" + tasks.getDateTime().getYear());
                txtTime.setText(tasks.getDateTime().getHours() + ":" + tasks.getDateTime().getMinutes());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "No reminder found", Toast.LENGTH_LONG).show();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
