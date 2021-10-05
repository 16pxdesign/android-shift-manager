package com.ruszala.fueltrack.ui.shifts.endshift;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.dialogs.DatePickerFragment;
import com.ruszala.fueltrack.dialogs.TimePickerFragment;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;
import com.ruszala.fueltrack.others.ShiftState;
import com.ruszala.fueltrack.ui.orders.map.OrderMapPreviewFragment;
import com.ruszala.fueltrack.ui.shifts.homeshift.HomeShiftFragment;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ShiftFragment extends Fragment {

    private ShiftViewModel mViewModel;
    private Button saveBtn;
    private Button cancelBtn;
    private EditText editTextStartDate;
    private EditText editTextStartTime;
    private EditText editTextEndDate;
    private EditText editTextEndTime;

    TimePickerFragment startTimePicker;
    TimePickerFragment endTimePicker;
    DatePickerFragment startDatePicker;
    DatePickerFragment endDatePicker;

    Serializable state;
    Shift shift;

    public static ShiftFragment newInstance() {
        return new ShiftFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.shift_fragment, container, false);

        editTextStartDate = (EditText) root.findViewById(R.id.editTextStartDate);
        editTextStartTime = (EditText) root.findViewById(R.id.editTextStartTime);
        editTextEndDate = (EditText) root.findViewById(R.id.editTextEndDate);
        editTextEndTime = (EditText) root.findViewById(R.id.editTextEndTime);

        saveBtn = root.findViewById(R.id.buttonSave);
        cancelBtn = root.findViewById(R.id.buttonCancel);

        SimpleDateFormat currentDate = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm");
        Date todayDate = new Date();

        state = getArguments().getSerializable("STATE");

        if (state == ShiftState.END) {
            shift = CurrentShift.getInstance().getShift();
            editTextStartDate.setText(currentDate.format(shift.getStartDate()));
            editTextStartTime.setText(currentTime.format(shift.getStartDate()));
            editTextEndDate.setText(currentDate.format(todayDate));
            editTextEndTime.setText(currentTime.format(todayDate));
        } else if (state == ShiftState.EDIT) {
            shift = (Shift) getArguments().getSerializable("SHIFTTOEDIT");
            editTextStartDate.setText(currentDate.format(shift.getStartDate()));
            editTextStartTime.setText(currentTime.format(shift.getStartDate()));
            if(shift.getEndDate()!=null){
                editTextEndDate.setText(currentDate.format(shift.getEndDate()));
                editTextEndTime.setText(currentTime.format(shift.getEndDate()));
            }else {
                editTextEndDate.setText(currentDate.format(todayDate));
                editTextEndTime.setText(currentTime.format(todayDate));
            }
        } else {
            editTextStartDate.setText(currentDate.format(todayDate));
            editTextStartTime.setText(currentTime.format(todayDate));
            editTextEndDate.setText(currentDate.format(todayDate));
            editTextEndTime.setText(currentTime.format(todayDate));
        }

        startDatePicker = new DatePickerFragment(shift.getStartDate());
        endDatePicker = new DatePickerFragment(shift.getStartDate());

        startTimePicker = new TimePickerFragment(shift.getStartDate());
        endTimePicker = new TimePickerFragment(shift.getEndDate());


        startTimePicker.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextStartTime.setText(hourOfDay + ":" + minute);
            }
        });

        endTimePicker.setListener(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextEndTime.setText(hourOfDay + ":" + minute);
            }
        });

        startDatePicker.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editTextStartDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        });

        endDatePicker.setListener(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                editTextEndDate.setText(dayOfMonth + "/" + month + "/" + year);
            }
        });


        editTextStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDatePicker.show(getFragmentManager(), "date picker");
            }
        });

        editTextStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTimePicker.show(getFragmentManager(), "time picker");
            }
        });

        editTextEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endDatePicker.show(getFragmentManager(), "date picker");
            }
        });

        editTextEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTimePicker.show(getFragmentManager(), "time picker");
            }
        });

        return root;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(ShiftViewModel.class);
        // TODO: Use the ViewModel

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mViewModel.saveCurrentShift(shift.getId(), editTextStartDate.getText().toString(), editTextStartTime.getText().toString(), editTextEndDate.getText().toString(), editTextEndTime.getText().toString());
                    getActivity().getSupportFragmentManager().popBackStack();
                } catch (Exception e) {
                    new AlertDialog.Builder(getActivity()).setMessage(e.getMessage()).setPositiveButton("Ok", null).show();
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
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
            ((MainActivity)getActivity()).changeTitle("Add or Edit shift");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
