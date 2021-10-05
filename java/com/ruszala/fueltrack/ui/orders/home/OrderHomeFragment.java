package com.ruszala.fueltrack.ui.orders.home;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.global.CurrentShift;
import com.ruszala.fueltrack.interfaces.OnNavFragmentInteractionListener;
import com.ruszala.fueltrack.interfaces.OrderHomeListener;
import com.ruszala.fueltrack.ui.orders.details.OrderEditFragment;
import com.ruszala.fueltrack.ui.orders.list.OrderListFragment;
import com.ruszala.fueltrack.ui.orders.map.OrderMapPreviewFragment;

public class OrderHomeFragment extends Fragment implements OrderHomeListener {

    private View view;
    private OrderMapPreviewFragment orderMapPreviewFragment;

    public static OrderHomeFragment newInstance() {
        return new OrderHomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        this.view = inflater.inflate(R.layout.order_home_fragment, container, false);
        getActivity().setTitle("Home");

        return this.view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //check for running shift
        if (CurrentShift.getInstance().isShiftRunning()) {
            //init fragments
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            if (!CurrentShift.getInstance().isOrderSet()) {
                fragmentTransaction.add(R.id.deliveryPanelContainer, new StartFragment());
            } else {
                OrderEditFragment orderEditFragment = new OrderEditFragment();
                fragmentTransaction.add(R.id.deliveryPanelContainer, orderEditFragment);
            }
            //load only for ORIENTATION_LANDSCAPE
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                orderMapPreviewFragment = new OrderMapPreviewFragment();
                fragmentTransaction.add(R.id.mapFragment, orderMapPreviewFragment);
            } else {
            }

            OrderListFragment fragmentOrderList = new OrderListFragment();
            fragmentTransaction.add(R.id.listfragmentContainer, fragmentOrderList);
            fragmentTransaction.commit();

        } else {
            //Finish if no shift start
            Toast.makeText(getActivity(), "Need start shift first", Toast.LENGTH_LONG).show();
            OnNavFragmentInteractionListener home = (OnNavFragmentInteractionListener) getActivity();
            home.changeFragment(R.id.startShiftFragment);
        }

    }

    //action to start delivery
    @Override
    public void onStartClick() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.deliveryPanelContainer, new OrderEditFragment()).commit();
    }

    //action to save delivery
    @Override
    public void onSaveOrder() {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.deliveryPanelContainer, new StartFragment()).commit();
    }

    //action to edit delivery
    @Override
    public void onEdit(Order order) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        OrderEditFragment orderEditFragment = new OrderEditFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ORD", order);
        bundle.putString("SAVEBTN", "Save");
        orderEditFragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.deliveryPanelContainer, orderEditFragment).commit();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        //remove maps to avoid loading in orientation change
        if (orderMapPreviewFragment != null) {
            getChildFragmentManager().beginTransaction().remove(orderMapPreviewFragment).commit();
            orderMapPreviewFragment = null;
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        //((MainActivity) getActivity()).changeTitle("Manage orders");

    }
}
