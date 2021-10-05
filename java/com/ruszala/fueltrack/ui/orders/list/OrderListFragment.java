package com.ruszala.fueltrack.ui.orders.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.adapters.OrderListAdapter;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.interfaces.OrderHomeListener;
import com.ruszala.fueltrack.interfaces.OnListIItemButtonClickListener;

import java.util.ArrayList;
import java.util.HashMap;

public class OrderListFragment extends Fragment implements OnListIItemButtonClickListener {

    private OrderListViewModel mViewModel;

    private RecyclerView mOrderRecycleView;
    private RecyclerView.Adapter mOrderListAdapter;
    private RecyclerView.LayoutManager mOrderListManager;
    View view;
    ArrayList<Order> orders;

    public static OrderListFragment newInstance() {
        return new OrderListFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.order_list_fragment, container, false);

        //init recyclerview
        orders = new ArrayList<>();
        initializeRW();

        return view;
    }

    //init recyclerview
    private void initializeRW() {
        mOrderRecycleView = view.findViewById(R.id.orderList);
        mOrderRecycleView.setNestedScrollingEnabled(false);
        mOrderRecycleView.setHasFixedSize(false);
        mOrderListManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mOrderRecycleView.setLayoutManager(mOrderListManager);
        mOrderListAdapter = new OrderListAdapter(orders);
        ((OrderListAdapter) mOrderListAdapter).setButtonClickListener(this);
        mOrderRecycleView.setAdapter(mOrderListAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(OrderListViewModel.class);

        //observer for recycler view
        mViewModel.getOrderList().observe(this, new Observer<HashMap<String, Order>>() {
            @Override
            public void onChanged(HashMap<String, Order> stringOrdersHashMap) {
                orders.clear();
                orders.addAll(new ArrayList<>(stringOrdersHashMap.values()));
                mOrderListAdapter.notifyDataSetChanged();
            }
        });
    }

    //action for edit
    @Override
    public void onEditClick(int i) {
        if(getParentFragment() instanceof OrderHomeListener)
            ((OrderHomeListener) getParentFragment()).onEdit(orders.get(i));

    }
    //action for delete
    @Override
    public void onDeleteClick(int i) {
        Order orderToDelete = orders.get(i);
        mViewModel.deleteOrder(orderToDelete);
    }
}
