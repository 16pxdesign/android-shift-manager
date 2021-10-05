package com.ruszala.fueltrack.ui.orders.list;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;

import java.util.HashMap;

public class OrderListViewModel extends ViewModel {

    private MutableLiveData<HashMap<String, Order>> orderList;

    public OrderListViewModel() {
        orderList = new MutableLiveData<>();
    }

    public LiveData<HashMap<String, Order>> getOrderList() {

        //fetch data from firebase using event listener
        Query rf = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("orders").orderByChild("shiftId").equalTo(CurrentShift.getInstance().getShift().getId());
        rf.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.v("ppc", "addValueEventListener onDataChange OrderListViewModel");
                HashMap<String, Order> objectHashMap = new HashMap<>();
                if(!dataSnapshot.exists()){
                    orderList.setValue(objectHashMap);
                    return;
                }

                for (DataSnapshot sub: dataSnapshot.getChildren()) {
                    Order order = sub.getValue(Order.class);
                    order.setId(sub.getKey());
                    objectHashMap.put(sub.getKey(), order);
                    orderList.setValue(objectHashMap);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return orderList;

    }

    public void deleteOrder(Order order) {
        DatabaseReference rf = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getUid()).child("orders").child(order.getId());
        rf.removeValue();
    }

}
