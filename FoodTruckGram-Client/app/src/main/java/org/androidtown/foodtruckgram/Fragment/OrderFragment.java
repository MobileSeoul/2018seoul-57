package org.androidtown.foodtruckgram.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidtown.foodtruckgram.Adapter.CustomerOrderMenuAdapter;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    private View view;

    public OrderFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_order, container, false);

        Bundle bundle = getArguments();
        FoodTruckInfo info = (FoodTruckInfo) bundle.getSerializable("foodtruckInfo");

        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.customerOrderMenuRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        CustomerOrderMenuAdapter adapter = new CustomerOrderMenuAdapter(info, getContext());
        recyclerView.setAdapter(adapter);

        return view;
    }



}
