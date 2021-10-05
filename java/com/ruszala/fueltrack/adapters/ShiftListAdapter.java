package com.ruszala.fueltrack.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.domain.Shift;
import com.ruszala.fueltrack.interfaces.OnListIItemButtonClickListener;

import java.util.ArrayList;
/**
 * Adapter and Adapter holder for recyclerview that display list of shifts
 */
public class ShiftListAdapter extends RecyclerView.Adapter<ShiftListAdapter.ShiftListRecycleViewHolder> {

    ArrayList<Shift> shifts;
    OnListIItemButtonClickListener buttonClickListener;


    public ShiftListAdapter(ArrayList<Shift> shifts) {
        this.shifts = shifts;
    }

    //Init external listener to communicate with fragment
    public void setButtonClickListener(OnListIItemButtonClickListener listener){
        buttonClickListener = listener;
    }


    @NonNull
    @Override
    public ShiftListRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shift,null,false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        ShiftListRecycleViewHolder viewHolder = new ShiftListRecycleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftListRecycleViewHolder holder, int position) {

        //Change color for each 2nd record
        if (position % 2 == 1)
            holder.view.setBackgroundColor(Color.GRAY);
        else
            holder.view.setBackgroundColor(Color.TRANSPARENT);

        //Pass values to item views
        holder.startDate.setText(Shift.getDateString(shifts.get(position).getStartDate()));
        holder.startTime.setText(Shift.getTimeString(shifts.get(position).getStartDate()));
        holder.endDate.setText(Shift.getDateString(shifts.get(position).getEndDate()));
        holder.endTime.setText(Shift.getTimeString(shifts.get(position).getEndDate()));

    }

    @Override
    public int getItemCount() {
        if(shifts==null){
            return 0;
        }
        return shifts.size();
    }

    public class ShiftListRecycleViewHolder extends RecyclerView.ViewHolder {
        public TextView startDate;
        public TextView startTime;
        public TextView endDate;
        public TextView endTime;
        public Button editBtn;
        public Button deleteBtn;

        public View view;

        public ShiftListRecycleViewHolder(View view) {
            super(view);
            this.view = view;
            startDate = view.findViewById(R.id.itemStartDate);
            startTime = view.findViewById(R.id.itemStartTime);
            endDate = view.findViewById(R.id.itemEndDate);
            endTime = view.findViewById(R.id.itemEndTime);
            this.editBtn = view.findViewById(R.id.editShiftButton);
            this.deleteBtn = view.findViewById(R.id.deleteShiftButton);

            //set listeners for buttons and fire external listener
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListener.onDeleteClick(getAdapterPosition());
                }
            });

           editBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buttonClickListener.onEditClick(getAdapterPosition());
                }
            });
        }




    }

}

