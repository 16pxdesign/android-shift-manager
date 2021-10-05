package com.ruszala.fueltrack.ui.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<Integer> totalOrders;
    private MutableLiveData<Integer> totalShifts;

    public HomeViewModel() {
        totalOrders = new MutableLiveData<>();
        totalShifts = new MutableLiveData<>();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        //Setup Live data for totalOrders
        DatabaseReference orders = FirebaseDatabase.getInstance().getReference(uid).child("orders");
        orders.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalOrders.setValue((int) dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //Setup Live data for totalShifts
        DatabaseReference shifts = FirebaseDatabase.getInstance().getReference(uid).child("shifts");
        shifts.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                totalShifts.setValue((int) dataSnapshot.getChildrenCount());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }


    public LiveData<Integer> getTotalOrders() {

        return totalOrders;
    }

    public LiveData<Integer> getTotalShifts() {

        return totalShifts;
    }
}