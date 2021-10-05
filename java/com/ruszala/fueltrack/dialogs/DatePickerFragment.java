package com.ruszala.fueltrack.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.Date;

/**
 * Custom date picker fragment to handle choosing date
 */
public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener mListener;
    private Context context;
    Date date;

    public DatePickerFragment(Date date) {
        this.date = date;
    }

    public void setListener(DatePickerDialog.OnDateSetListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        int year = instance.get(Calendar.YEAR);
        int month = instance.get(Calendar.MONTH);
        int day = instance.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(context,mListener,year,month,day);

    }


}
