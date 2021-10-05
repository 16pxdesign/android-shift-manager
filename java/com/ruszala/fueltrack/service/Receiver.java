package com.ruszala.fueltrack.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;
import com.ruszala.fueltrack.service.TimingService;
import com.ruszala.fueltrack.ui.shifts.endshift.ShiftViewModel;

import java.util.Date;

/**
 * Receiver class to respond on service notification action
 */
public class Receiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("mylog", "Receiver onReceive: ");


        Intent service=new Intent(context, TimingService.class);
        IBinder binder = peekService(context, new Intent(context, TimingService.class));

        TimingService ref = ((TimingService.LocalBinder) binder).getService();
        ref.stopByNotification();
        Shift shift = CurrentShift.getInstance().getShift();
        new ShiftViewModel().saveCurrentShift();

    }
}
