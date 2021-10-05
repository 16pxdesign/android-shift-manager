package com.ruszala.fueltrack.interfaces;

import com.ruszala.fueltrack.domain.Order;

public interface OrderHomeListener {
    void onStartClick();
    void onSaveOrder();
    void onEdit(Order order);
}
