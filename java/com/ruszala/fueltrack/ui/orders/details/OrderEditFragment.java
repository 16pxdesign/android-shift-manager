package com.ruszala.fueltrack.ui.orders.details;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.ruszala.fueltrack.R;
import com.ruszala.fueltrack.domain.Order;
import com.ruszala.fueltrack.interfaces.OrderHomeListener;
import com.ruszala.fueltrack.ui.orders.sms.SendMessageFragment;

import java.util.Arrays;

public class OrderEditFragment extends Fragment {

    private OrderEditViewModel mViewModel;
    private EditText editTextPhone;
    private EditText editTextPrice;
    private ToggleButton chipCashOrder;
    private Button deliveredBtn;
    private LinearLayout sentMSG;
    private View view;
    private AutocompleteSupportFragment autocompleteFragment;
    private EditText autocompliteEditText;


    public static OrderEditFragment newInstance() {
        return new OrderEditFragment();
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_edit_fragment, container, false);

        editTextPhone = view.findViewById(R.id.editTextPhone);
        editTextPrice = view.findViewById(R.id.editTextPrice);
        chipCashOrder = view.findViewById(R.id.chipCashOrder);
        deliveredBtn = view.findViewById(R.id.buttonDelivered);
        sentMSG = view.findViewById(R.id.smsContainer);

        //init autocomplete google maps
        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.google_maps_api));
        }
        autocompleteFragment = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplite_fragment);
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG, Place.Field.ADDRESS));
        autocompliteEditText = autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_input);
        autocompliteEditText.setTextColor(Color.GRAY);
        autocompliteEditText.setBackground(getResources().getDrawable(R.drawable.underline, getActivity().getTheme()));
        autocompleteFragment.setHint("");
        autocompleteFragment.getView().findViewById(R.id.places_autocomplete_search_button).setVisibility(View.GONE);

        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //model
        mViewModel = ViewModelProviders.of(this).get(OrderEditViewModel.class);

        //init fragments and check for incoming data
        if (getArguments() != null) {
            Order ord = (Order) getArguments().getSerializable("ORD");
            String btnMsg = getArguments().getString("SAVEBTN");
            mViewModel.setOrder(ord);
            deliveredBtn.setText(btnMsg);
            sentMSG.setVisibility(View.INVISIBLE);
        } else {
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            SendMessageFragment sendMessageFragment = new SendMessageFragment();
            fragmentTransaction.replace(R.id.smsContainer, sendMessageFragment).commit();
        }

        //set observer for textviews
        mViewModel.order.observe(this, new Observer<Order>() {
            @Override
            public void onChanged(Order order) {
                if (order != null) {
                    autocompleteFragment.setText(order.getAdders());
                    editTextPhone.setText(order.getPhone());
                    editTextPrice.setText(order.getPriceString());
                    chipCashOrder.setChecked(order.isCash());

                }
            }
        });


        //Click listeners and others
        deliveredBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModel.saveOrder();
                if (getParentFragment() instanceof OrderHomeListener) {
                    ((OrderHomeListener) getParentFragment()).onSaveOrder();
                }
            }
        });

        chipCashOrder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                mViewModel.setCcCash(isChecked);
            }
        });

        editTextPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setPhone(s.toString());
            }
        });

        editTextPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                mViewModel.setPrice(s.toString());
            }
        });

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(final Place place) {
                if (place.getAddress() != null) {

                    //Google maps override setText
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            autocompliteEditText.setText(place.getAddress());
                        }
                    }, 300);

                    mViewModel.setAddress(place.getAddress());
                    if (place.getLatLng() != null) {
                        mViewModel.setPlace(place.getLatLng());
                    }
                }
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.d("mylog", "HomeFragment onError: " + status);
                autocompleteFragment.setText("");
                mViewModel.setAddress(null);
            }
        });


    }

}
