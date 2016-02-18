package com.reminder.three;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class addNewReminderActivity extends AppCompatActivity{

//    private static final String TIME_PATTERN = "HH:mm";
    private TasksDataSource datasource;
    private Tasks task;
    private long taskID;
    private Calendar c = null;
    private String strTask;
    private Button btnDate;
    private Button btnTime;
    private Button btnLocation;
    private EditText edittextTask;
    private TextView dateView;
    private TextView timeView;
    private boolean updateTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_reminder);

        btnDate = (Button) findViewById(R.id.btnDate);
        btnTime = (Button) findViewById(R.id.btnTime);
        btnLocation = (Button) findViewById(R.id.BtnLocation);
        edittextTask = (EditText) findViewById(R.id.edittextTask);
        dateView = (TextView) findViewById(R.id.dateView);
        timeView = (TextView) findViewById(R.id.timeView);

        if(getIntent().getExtras() != null) {
            taskID = getIntent().getLongExtra("taskID", -1);
            updateTask = true;
            if(taskID != -1) {
                DecimalFormat formatter = new DecimalFormat("00");
                datasource = new TasksDataSource(this);
                try {
                    datasource.open();
                    task = datasource.retrieveTask(taskID);
                    edittextTask.setText(task.getTask());

                    dateView.setText(formatter.format(task.getDateTime().getDay()) + "/" + formatter.format(task.getDateTime().getDay()) + "/" + formatter.format(task.getDateTime().getYear()));
                    timeView.setText(formatter.format(task.getDateTime().getHours()) + formatter.format(task.getDateTime().getMinutes()) + "HR");

                    if (task.getDateTime().getHours() <= 12) {
                        timeView.setText(timeView.getText() + " | " + formatter.format(task.getDateTime().getHours()) + ":" + formatter.format(task.getDateTime().getMinutes()) + "AM");
                    } else {
                        timeView.setText(timeView.getText() + " | " + formatter.format(task.getDateTime().getHours() - 12) + ":" + formatter.format(task.getDateTime().getMinutes()) + "PM");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        btnLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                startActivity(intent);
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "Pick a Date");
            }
        });

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"Pick a Time");
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_save) {
            datasource = new TasksDataSource(this);
            try {
                datasource.open();
                strTask = edittextTask.getText().toString();
                if (updateTask) {
                    datasource.updateTasks(taskID, strTask, c, 0, 0);
                } else {
                    //Toast.makeText(this, strTask, Toast.LENGTH_LONG).show();
                    datasource.createTask(strTask, c, 0, 0);
                }
                finish();
            } catch (SQLException e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //Create and return a new instance of TimePickerDialog
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            //Do something with the date chosen by the user
            TextView dateView = (TextView) getActivity().findViewById(R.id.dateView);
            DecimalFormat formatter = new DecimalFormat("00");

            dateView.setText(formatter.format(day) + "/" + formatter.format(month + 1) + "/" + String.valueOf(year));
        }
    }

    public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState){
            //Use the current time as the default values for the time picker
            c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            //Create and return a new instance of TimePickerDialog
            return new TimePickerDialog(getActivity(),this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        //onTimeSet() callback method
        public void onTimeSet(TimePicker view, int hourOfDay, int minute){
            //Do something with the user chosen time
            //Get reference of host activity (XML Layout File) TextView widget
            TextView timeView = (TextView) getActivity().findViewById(R.id.timeView);
            DecimalFormat formatter = new DecimalFormat("00");
            //Display the user changed time on TextView
            timeView.setText(formatter.format(hourOfDay) + formatter.format(minute) + "HR");

            if(hourOfDay <= 12) {
                timeView.setText(timeView.getText() + " | " + formatter.format(hourOfDay) + ":" + formatter.format(minute) + "AM");
            }
            else {
                timeView.setText(timeView.getText() + " | " + formatter.format(hourOfDay - 12) +  ":" + formatter.format(minute) + "PM");
            }
        }
    }
}
