package com.ruszala.fueltrack.ui.orders.home;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.interfaces.OrderHomeListener;

/*
*Fragment with start button for delivery
 */
public class StartFragment extends Fragment {


    public StartFragment() {
    }

    public static StartFragment newInstance() {
        return new StartFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start, container, false);
        Button startBtn = view.findViewById(R.id.buttonStartDelivery);

        //click listener
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //fire start listener on paernet fragment
                Fragment parentFragment = getParentFragment();
                if(parentFragment instanceof OrderHomeListener){
                    ((OrderHomeListener) parentFragment).onStartClick();
                }
            }
        });
        return view;
    }

}
