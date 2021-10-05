package com.ruszala.fueltrack.ui.home;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;

import org.w3c.dom.Text;

import java.util.Arrays;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private TextView totalOrders, totalShifts;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        totalOrders = root.findViewById(R.id.total_orders);
        totalShifts = root.findViewById(R.id.total_shifts);
        ((MainActivity)getActivity()).changeTitle("Home");

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //model
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);

        //bind textviews to model
        homeViewModel.getTotalOrders().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                totalOrders.setText(integer.toString());
            }
        });

        homeViewModel.getTotalShifts().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                totalShifts.setText(integer.toString());
            }
        });
    }
}