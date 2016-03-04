package com.reminder.three;

import java.util.Date;

/**
 * Created by arbalestx on 15/2/16.
 */
public class Tasks {
    private long id;
    private String task;
    private Date dateTime;
    private double lat;
    private double lng;

    public Tasks() {
        super();
    }
    public Tasks(long id, String task, Date dateTime) {
        this.id = id;
        this.task = task;
        this.dateTime = dateTime;
    }

    public long getId() {
        return id;
    }
    public String getTask() {
        return task;
    }
    public Date getDateTime() {
        return dateTime;
    }
    public double getLat() {
        return lat;
    }
    public double getLng() {
        return lng;
    }

    public void setId(long id) {
        this.id = id;
    }
    public void setTask(String task) {
        this.task = task;
    }
    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }
    public void setLng(double lng) {
        this.lng = lng;
    }

    @Override
    public String toString() {
        return this.task;
    }
}
