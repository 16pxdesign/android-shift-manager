package com.ruszala.fueltrack.ui.shifts.create;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.dialogs.DatePickerFragment;
import com.ruszala.fueltrack.dialogs.TimePickerFragment;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.ui.shifts.homeshift.HomeShiftFragment;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;

public class NewShiftFragment extends Fragment implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private NewShiftViewModel mViewModel;
    private Button startBtn;
    private Button cancelBtn;
    private EditText editTextDate;
    private EditText editTextTime;

    TimePickerFragment timePicker;
    DatePickerFragment datePicker;

    public static NewShiftFragment newInstance() {
        return new NewShiftFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root =  inflater.inflate(R.layout.new_shift_fragment, container, false);
        editTextDate = (EditText) root.findViewById(R.id.editTextStartDate);
        editTextTime = (EditText) root.findViewById(R.id.editTextStartTime);
        startBtn = root.findViewById(R.id.buttonStart);
        cancelBtn = root.findViewById(R.id.buttonCancel);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        Date todayDate = new Date();
        editTextDate.setText(currentDate.format(todayDate));
        editTextTime.setText(currentTime.format(todayDate));

        //init pickers
        datePicker = new DatePickerFragment(todayDate);
        timePicker = new TimePickerFragment(todayDate);

        //init listeners
        timePicker.setListener(this);
        datePicker.setListener(this);



        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePicker.show(getFragmentManager(), "date picker");
            }
        });

        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePicker.show(getFragmentManager(), "time picker");
            }
        });

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewShiftViewModel.class);

        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mViewModel.saveShift(editTextDate.getText().toString(),editTextTime.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStack();
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).setPositiveButton("Ok",null).show();
                }

            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });

    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        editTextTime.setText(hourOfDay+":"+minute);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        editTextDate.setText(dayOfMonth+"/"+month+"/"+year);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        if(childFragment instanceof HomeShiftFragment){

        }else {
            ((MainActivity)getActivity()).changeTitle("Add or Edit shift");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        timePicker.setListener(null);
        datePicker.setListener(null);
    }
}
