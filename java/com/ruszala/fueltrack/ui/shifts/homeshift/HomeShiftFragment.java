package com.ruszala.fueltrack.ui.shifts.homeshift;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ruszala.fueltrack.MainActivity;
import com.ruszala.fueltrack.interfaces.OnNavFragmentInteractionListener;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.global.CurrentShift;
import com.ruszala.fueltrack.others.ShiftState;

public class HomeShiftFragment extends Fragment {

    Button startBtn;
    Button endBtn;
    Button historyBtn;

    public static HomeShiftFragment newInstance() {
        return new HomeShiftFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.start_shift_fragment, container, false);

        startBtn = root.findViewById(R.id.start);
        endBtn = root.findViewById(R.id.end);
        historyBtn = root.findViewById(R.id.last);
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnNavFragmentInteractionListener home = (OnNavFragmentInteractionListener)getActivity();
                home.changeFragment(R.id.newShiftFragment);
            }
        });
        endBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnNavFragmentInteractionListener home = (OnNavFragmentInteractionListener)getActivity();
                Bundle bundle = new Bundle();
                bundle.putSerializable("STATE",ShiftState.END);
                home.changeFragment(R.id.ShiftFragment, bundle);
            }
        });
        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OnNavFragmentInteractionListener home = (OnNavFragmentInteractionListener)getActivity();
                home.changeFragment(R.id.historyShiftFragment);

            }
        });

        startBtn.setEnabled(false);
        endBtn.setEnabled(false);

        return root;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ((MainActivity)getActivity()).changeTitle("Manage your shifts");

        CurrentShift.getInstance().getShiftLifeData().observe(this, new Observer<Shift>() {
            @Override
            public void onChanged(Shift shift) {
                Log.v("ppp", "observe onChanged");
                if(CurrentShift.getInstance().getShiftLifeData().getValue()==null){
                    startBtn.setEnabled(true);
                    endBtn.setEnabled(false);
                }else {
                    startBtn.setEnabled(false);
                    endBtn.setEnabled(true);
                }

            }
        });

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
