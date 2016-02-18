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
import java.text.DecimalFormat;

public class ViewReminder extends AppCompatActivity {

    private long id;
    private Tasks task;
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

        updateView();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addNewReminderActivity.class);
                intent.putExtra("taskID", id);
                startActivity(intent);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateView();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    public void updateView() {
        if(id != -1) {
            DecimalFormat formatter = new DecimalFormat("00");
            datasource = new TasksDataSource(this);
            try {
                datasource.open();
                task = datasource.retrieveTask(id);
                txtTask.setText(task.getTask());
                txtDate.setText(formatter.format(task.getDateTime().getDay()) + "/" + formatter.format(task.getDateTime().getMonth()) + "/" + formatter.format(task.getDateTime().getYear()));
                txtTime.setText(formatter.format(task.getDateTime().getHours()) + formatter.format(task.getDateTime().getMinutes()) + "HR");

                if(task.getDateTime().getHours() <= 12) {
                    txtTime.setText(txtTime.getText() + " | " + formatter.format(task.getDateTime().getHours()) + ":" + formatter.format(task.getDateTime().getMinutes()) + "AM");
                }
                else {
                    txtTime.setText(txtTime.getText() + " | " + formatter.format(task.getDateTime().getHours() - 12) + ":" + formatter.format(task.getDateTime().getMinutes()) + "PM");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            Toast.makeText(this, "No reminder found", Toast.LENGTH_LONG).show();
        }
    }
}
