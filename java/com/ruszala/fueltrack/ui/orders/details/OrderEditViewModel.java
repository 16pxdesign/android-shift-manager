package com.ruszala.fueltrack.ui.orders.details;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.global.CurrentShift;

public class OrderEditViewModel extends ViewModel {
    // TODO: Implement the ViewModel

    MutableLiveData<Order> order;

    public OrderEditViewModel() {
        order = new MutableLiveData<>();
        if(CurrentShift.getInstance().getOrder().getValue()==null){
            order.setValue(new Order());
            CurrentShift.getInstance().setOrder(order.getValue());
        }else {
            order.setValue(CurrentShift.getInstance().getOrder().getValue());
        }

    }

    //save order into firebase
    public void saveOrder() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference rf;
        if(order.getValue().getId()==null){
            rf = FirebaseDatabase.getInstance().getReference(uid).child("orders").push();

        }else {
            String id = order.getValue().getId();
            rf = FirebaseDatabase.getInstance().getReference(uid).child("orders").child(id);
        }
        order.getValue().setShiftId(CurrentShift.getInstance().getShift().getId());
        rf.setValue(order.getValue());
        order.setValue(null);
        CurrentShift.getInstance().setOrder(null);
    }

    public void setAddress(String address) {
        order.getValue().setAdders(address);
        CurrentShift.getInstance().setOrder(order.getValue());
    }

    public void setPlace(LatLng position){
        order.getValue().setPosition(position);
        CurrentShift.getInstance().setOrder(order.getValue());
    }

    public void setCcCash(boolean isChecked) {
        order.getValue().setCash(isChecked);
        CurrentShift.getInstance().setOrder(order.getValue());
    }

    public void setPhone(String toString) {
        order.getValue().setPhone(toString);
        CurrentShift.getInstance().setOrder(order.getValue());
    }

    public void setPrice(String toString) {
        try{
            Double aDouble= Double.valueOf(toString);
            order.getValue().setPrice(aDouble);
            CurrentShift.getInstance().setOrder(order.getValue());
        }catch (Exception e){

        }

    }

    //set global order to order
    public void setOrder(Order ord) {
        order.setValue(ord);
        CurrentShift.getInstance().setOrder(ord);
    }


}
