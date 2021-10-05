package com.ruszala.fueltrack.ui.shifts.history;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.interfaces.OnNavFragmentInteractionListener;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.adapters.ShiftListAdapter;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.interfaces.OnListIItemButtonClickListener;
import com.ruszala.fueltrack.others.ShiftState;
import com.ruszala.fueltrack.ui.orders.map.OrderMapPreviewFragment;
import com.ruszala.fueltrack.ui.shifts.homeshift.HomeShiftFragment;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryShiftFragment extends Fragment implements OnListIItemButtonClickListener {

    private HistoryShiftViewModel mViewModel;
    private RecyclerView mshiftRecycleView;
    private RecyclerView.Adapter mShiftListAdapter;
    private RecyclerView.LayoutManager mShiftListManager;
    View root;

    ArrayList<Shift> shifts;


    public static HistoryShiftFragment newInstance() {
        return new HistoryShiftFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        root =  inflater.inflate(R.layout.history_shift_fragment, container, false);
        //init recyclerview
        shifts = new ArrayList<>();
        initializeRW();
        return root;
    }

    //init recyclerview
    private void initializeRW() {
        mshiftRecycleView = root.findViewById(R.id.shiftlist);
        mshiftRecycleView.setNestedScrollingEnabled(false);
        mshiftRecycleView.setHasFixedSize(false);
        mShiftListManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false);
        mshiftRecycleView.setLayoutManager(mShiftListManager);
        mShiftListAdapter = new ShiftListAdapter(shifts);
        ((ShiftListAdapter) mShiftListAdapter).setButtonClickListener(this);
        mshiftRecycleView.setAdapter(mShiftListAdapter);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HistoryShiftViewModel.class);
        //set observer for adapter
        mViewModel.getShifts().observe(this, new Observer<HashMap<String, Shift>>() {
            @Override
            public void onChanged(HashMap<String, Shift> stringShiftHashMap) {
                shifts.clear();
                shifts.addAll(new ArrayList<>(stringShiftHashMap.values()));
                shifts = Shift.sortByEndDate(shifts);
                mShiftListAdapter.notifyDataSetChanged();
            }
        });
    }

    //handle action for edit button
    @Override
    public void onEditClick(int adapterPosition) {
        OnNavFragmentInteractionListener home = (OnNavFragmentInteractionListener)getActivity();
        Bundle bundle = new Bundle();
        bundle.putSerializable("STATE", ShiftState.EDIT);
        bundle.putSerializable("SHIFTTOEDIT", shifts.get(adapterPosition));
        home.changeFragment(R.id.ShiftFragment, bundle);
    }

    //handle action for delete button
    @Override
    public void onDeleteClick(int adapterPosition) {
        mViewModel.deleteShift(shifts.get(adapterPosition));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ((ShiftListAdapter) mShiftListAdapter).setButtonClickListener(null);
    }

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
            ((MainActivity)getActivity()).changeTitle("History shifts");

    }
}
