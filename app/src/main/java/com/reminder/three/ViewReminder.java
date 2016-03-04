package com.reminder.three;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ViewReminder extends AppCompatActivity {

    private long id;
    private Tasks task;
    private TasksDataSource datasource;
    private TextView txtTask;
    private TextView txtDate;
    private TextView txtTime;
    private Calendar c = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_reminder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        txtTask = (TextView) findViewById(R.id.txtViewTask);
        txtDate = (TextView) findViewById(R.id.txtDate);
        txtTime = (TextView) findViewById(R.id.txtTime);

        c = new GregorianCalendar();

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

                c.setTime(task.getDateTime());
                txtDate.setText(formatter.format(c.get(Calendar.DAY_OF_MONTH)) + "/" + formatter.format(c.get(Calendar.MONTH)+1) + "/" + formatter.format(c.get(Calendar.YEAR)));
                txtTime.setText(formatter.format(c.get(Calendar.HOUR_OF_DAY)) + formatter.format(c.get(Calendar.MINUTE)) + "HR");

                if(c.get(Calendar.HOUR_OF_DAY) <= 12) {
                    txtTime.setText(txtTime.getText() + " | " + formatter.format(c.get(Calendar.HOUR_OF_DAY)) + ":" + formatter.format(c.get(Calendar.MINUTE)) + "AM");
                }
                else {
                    txtTime.setText(txtTime.getText() + " | " + formatter.format(c.get(Calendar.HOUR_OF_DAY) - 12) + ":" + formatter.format(c.get(Calendar.MINUTE)) + "PM");
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
