package com.ruszala.fueltrack.ui.orders.map;

import android.content.Context;
import android.database.Observable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.FirebaseDatabase;
import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.global.CurrentShift;
import com.ruszala.fueltrack.interfaces.OnNavFragmentInteractionListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderMapPreviewFragment extends Fragment implements OnMapReadyCallback {

    private OrderMapPreviewViewModel mViewModel;
    private SupportMapFragment fragment;
    private GoogleMap map;

    public static OrderMapPreviewFragment newInstance() {
        return new OrderMapPreviewFragment();
    }

    View root;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.order_map_preview_fragment, container, false);
        return root;
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        //check for parrent fragment and set title if required
        if(childFragment instanceof OrderMapPreviewFragment){

        }else {
            ((MainActivity)getActivity()).changeTitle("Current orders map");
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderMapPreviewViewModel.class);

        //prevent running fragment if shift is not set
        if (!CurrentShift.getInstance().isShiftRunning()) {
            Toast.makeText(getActivity(), "Need start shift first", Toast.LENGTH_LONG).show();
            OnNavFragmentInteractionListener home = (OnNavFragmentInteractionListener) getActivity();
            home.changeFragment(R.id.startShiftFragment);
            return;
        }
        //init google map fragment to view
        FragmentManager fm = getChildFragmentManager();
        fragment = (SupportMapFragment) fm.findFragmentById(R.id.map);
        if (fragment == null) {
            fragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.map, fragment).commit();

        }
        //set listener for map
        fragment.getMapAsync(this);


    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {
        map = googleMap;
        Log.d("myorie", "OrderMapPreviewFragment onMapReady: ");

        firebaseListener();

    }



    private void firebaseListener() {
        //observe model ordel list and bind data to map
        mViewModel.getOrderList().observe(this, new Observer<HashMap<String, Order>>() {
            @Override
            public void onChanged(HashMap<String, Order> stringOrderHashMap) {
                map.clear();
                if(stringOrderHashMap.values().size()>0){

                    LatLngBounds.Builder builder;
                    builder = new LatLngBounds.Builder();

                    for(Order m : stringOrderHashMap.values()){
                        MarkerOptions position = new MarkerOptions().position(m.getPosition());
                        Marker marker = map.addMarker(position);
                        builder.include(marker.getPosition());
                    }
                    final LatLngBounds bounds = builder.build();
                    map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
                }

            }
        });
    }

    //test init method
    private void init(GoogleMap googleMap) {
        Log.d("mylog", "OrderMapPreviewFragment onMapReady: ");

        ArrayList<Order> list = new ArrayList<>();
        Order order = new Order();
        order.setPosition(new LatLng(56.461430, -2.968110));
        Order order2 = new Order();
        order2.setPosition(new LatLng(56.484008, -2.993350));
        Order order3 = new Order();
        order3.setPosition(new LatLng(56.396051, -3.054269));
        list.add(order);
        list.add(order2);
        list.add(order3);


        LatLngBounds.Builder builder;
        builder = new LatLngBounds.Builder();

        //marker.remove();

        for (Order o : list) {
            MarkerOptions position = new MarkerOptions().position(o.getPosition());
            Marker marker = map.addMarker(position);
            marker.setTag("xd");
            Log.d("mylog", "OrderMapPreviewFragment addMarker: " + marker.getId() + marker.getTag().toString());

            builder.include(o.getPosition());
        }


        builder.include(order2.getPosition());
        builder.include(order3.getPosition());
        final LatLngBounds bounds = builder.build();
        map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        map = null;
        Log.d("myorie", "OrderMapPreviewFragment onDestroyView: ");
    }


}
