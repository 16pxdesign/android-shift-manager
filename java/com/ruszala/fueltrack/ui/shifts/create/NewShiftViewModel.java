package com.ruszala.fueltrack.ui.shifts.create;

import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;

import java.util.ArrayList;
import java.util.HashMap;

public class NewShiftViewModel extends ViewModel {

    //save shift to firebase
    public void saveShift(String text, String text1) throws Exception {
        Shift shift = new Shift();
        shift.setStartDate(text, text1);
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(uid).child("shifts").child(shift.getId());
        rf.setValue(shift);
    }
}
