package com.ruszala.fueltrack.ui.shifts.history;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruszala.fueltrack.domain.Shift;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HistoryShiftViewModel extends ViewModel {
    public MutableLiveData<HashMap<String, Shift>> shiftList = new MutableLiveData<>();


    public MutableLiveData<HashMap<String, Shift>> getShifts(){
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("shifts");
        rf.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("pppc", "addValueEventListener onDataChange HistoryShiftViewModel");

                HashMap<String, Shift> map = new HashMap<>();
                if(!dataSnapshot.exists()){
                    shiftList.setValue(map);
                    return;
                }

                for (DataSnapshot sub: dataSnapshot.getChildren()) {
                    Shift shift = sub.getValue(Shift.class);
                    shift.setId(sub.getKey());
                    map.put(sub.getKey(), shift);
                    shiftList.setValue(map);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return shiftList;
    }

    public void deleteShift(Shift shift) {
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("shifts").child(shift.getId());
        rf.removeValue();

    }
}
