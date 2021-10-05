package com.ruszala.fueltrack.domain;

import android.util.Log;

import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;
/**
 * Model class represent shift
 */
public class Shift implements Serializable {
    String id;
    Date startDate;
    Date endDate;

    public Shift() {
        id = UUID.randomUUID().toString();
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getStartDate() {
        return startDate;
    }

    //To string Date
    public static String getDateString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        String s;
        try{
            s = format.format(date);
        }catch (Exception e){
            s = "----";
        }
        return s;
    }

    //To string Time
    public static String getTimeString(Date date){
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        String s;
        try{
            s = format.format(date);
        }catch (Exception e){
            s = "";
        }
        return s;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setStartDate(String date, String time) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            startDate = format.parse(date + " " + time);

        } catch (ParseException e) {
            throw new Exception("Bad convert. \nWrong date or time format \ndd/MM/yyyy HH:mm");
        }

    }

    public void setEndDate(String date, String time) throws Exception {

        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        try {
            endDate = format.parse(date + " " + time);

        } catch (ParseException e) {
            throw new Exception("Bad convert. \nWrong date or time format \ndd/MM/yyyy HH:mm");
        }

    }

    //sorting method to sort shifts by end date
    public static ArrayList<Shift> sortByEndDate(ArrayList<Shift> shifts){
        Collections.sort(shifts, new Comparator<Shift>() {
            @Override
            public int compare(Shift o1, Shift o2) {
                if(o1.getEndDate()==null)
                    return (o2.getEndDate() == null) ? 0 : -1;
                if(o2.getEndDate()==null)
                    return 1;
                int i = o2.getEndDate().compareTo(o1.getEndDate());
                return i;
            }
        });

        return shifts;

    }


}
