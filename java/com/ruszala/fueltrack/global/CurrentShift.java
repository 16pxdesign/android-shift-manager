package com.ruszala.fueltrack.global;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.domain.Shift;

/**
 * Its global singleton class to hold current running and processing shift
 * Application use this class to sync all fragments and features.
 * It is substitute for sharedPrefs or cache data
 */
public class CurrentShift {

    public static CurrentShift mInstance;
    public static MutableLiveData<Shift> shift;
    private static MutableLiveData<Order> currentOrder;

    public CurrentShift() {
        shift = new MutableLiveData<>();
        currentOrder = new MutableLiveData<>();
    }

    //singleton
    public static CurrentShift getInstance() {
        if (mInstance == null) {
            mInstance = new CurrentShift();

            firebaseShift();
        }

        return mInstance;
    }



    //load current running shift form firebase if exist
    private static void firebaseShift() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("shifts");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("ppp", "addValueEventListener onDataChange Shift");
                for (DataSnapshot sub : dataSnapshot.getChildren()) {
                    if (sub.child("endDate").getValue() == null) {
                        Shift value = sub.getValue(Shift.class);
                        value.setId(sub.getKey());
                        shift.setValue(value);
                        return;
                    }
                }
                shift.setValue(null);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.v("ppp", "addValueEventListener onCancelled Shift");

            }
        });

    }

    public MutableLiveData<Shift> getShiftLifeData() {
        return shift;
    }

    public Shift getShift() {
        return shift.getValue();
    }


    public boolean isShiftRunning(){
        if(shift.getValue()==null)
            return false;
        return true;
    }

    public boolean isOrderSet(){
        if(currentOrder.getValue()==null)
            return false;
        return true;
    }

    //clean all data and delete current instance
    public void clean(){
        shift = new MutableLiveData<>();
        currentOrder = new MutableLiveData<>();
        mInstance = null;
    }

    public MutableLiveData<Order> getOrder() {
        return currentOrder;
    }

    public void setOrder(Order order) {
        this.currentOrder.setValue(order);
    }
}


