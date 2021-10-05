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
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.interfaces.OnListIItemButtonClickListener;

import java.util.ArrayList;

/**
 * Adapter and Adapter holder for recyclerview that display list of orders
 */
public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListRecycleViewHolder> {


    ArrayList<Order> orders;
    OnListIItemButtonClickListener buttonClickListener;

    public OrderListAdapter(ArrayList<Order> orders) {
        this.orders = orders;
    }

    //Init external listener to communicate with fragment
    public void setButtonClickListener(OnListIItemButtonClickListener listener) {
        buttonClickListener = listener;
    }


    @NonNull
    @Override
    public OrderListRecycleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        view.setLayoutParams(lp);

        OrderListAdapter.OrderListRecycleViewHolder viewHolder = new OrderListAdapter.OrderListRecycleViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderListRecycleViewHolder holder, int position) {

        //Change color for each 2nd record
        if (position % 2 == 1)
            holder.view.setBackgroundColor(Color.GRAY);
        else
            holder.view.setBackgroundColor(Color.TRANSPARENT);

        //Pass values to item views
        holder.addresTextView.setText(orders.get(position).getAdders());
        holder.priceTextView.setText(orders.get(position).getPriceString());
        holder.cardTextView.setText(orders.get(position).getCardString());
        holder.phoneTextView.setText(orders.get(position).getPhone());


    }

    @Override
    public int getItemCount() {
        if (orders == null) {
            return 0;
        }
        return orders.size();
    }

    public class OrderListRecycleViewHolder extends RecyclerView.ViewHolder {
        public View view;
        public TextView addresTextView;
        public TextView priceTextView;
        public TextView cardTextView;
        public TextView phoneTextView;
        public Button editBtn;
        public Button deleteBtn;

        public OrderListRecycleViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            addresTextView = view.findViewById(R.id.itemAddress);
            priceTextView = view.findViewById(R.id.itemPrice);
            phoneTextView = view.findViewById(R.id.itemPhone);
            cardTextView = view.findViewById(R.id.itemCashCard);
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
