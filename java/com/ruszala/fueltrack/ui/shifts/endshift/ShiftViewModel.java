package com.ruszala.fueltrack.ui.shifts.endshift;

import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;

import java.util.Date;

public class ShiftViewModel extends ViewModel {


    public void saveCurrentShift(String id, String startdate, String starttime, String enddate, String endtime) throws Exception {
        Shift shift = new Shift();
        shift.setStartDate(startdate, starttime);
        shift.setEndDate(enddate, endtime);
        saveShift(id, shift);
    }

    private void saveShift(String id, Shift shift) {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(uid).child("shifts").child(id);
        rf.setValue(shift);
    }

    public void saveCurrentShift() {
        Shift shift = CurrentShift.getInstance().getShift();
        shift.setEndDate(new Date());
        saveShift(shift.getId(),shift);

    }

}
