package com.reminder.three;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.sql.SQLException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TasksDataSource datasource;
    private ListView listView;
    private List<Tasks> values;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list1);
        registerForContextMenu(listView);

        datasource = new TasksDataSource(this);
        initializeList();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), addNewReminderActivity.class);
                startActivity(intent);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Tasks tasks = (Tasks) listView.getItemAtPosition(position);
                Intent intent = new Intent(getApplicationContext(), ViewReminder.class);
                intent.putExtra("taskID", tasks.getId());
                startActivity(intent);
            }
        });
    }

    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.delete:
                ArrayAdapter<Tasks> adapter = (ArrayAdapter<Tasks>) listView.getAdapter();
                Tasks task = null;
                if (listView.getAdapter().getCount() > 0) {
                    task = (Tasks) listView.getAdapter().getItem(0);
                    datasource.deleteTask(task);
                    adapter.remove(task);
                }
                initializeList();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ctxmain, menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initializeList();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

    private void initializeList() {
        try {
            datasource.open();
            values = datasource.getAllTasks();

            ArrayAdapter<Tasks> adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, values);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
