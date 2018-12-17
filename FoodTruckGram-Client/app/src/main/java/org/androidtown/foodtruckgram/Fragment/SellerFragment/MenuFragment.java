package org.androidtown.foodtruckgram.Fragment.SellerFragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.androidtown.foodtruckgram.Activity.SellerMenuEditActivity;
import org.androidtown.foodtruckgram.Adapter.SellerMenuListAdapter;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<MenuInfo> menuList;
    private SellerMenuListAdapter adapter;
    static String serverURL = "http://"+ HttpClient.ipAdress+HttpClient.serverPort + HttpClient.urlBase + "/s/insertMenu";
    FoodTruckInfo foodTruckInfo;
    public MenuFragment(){

    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_menu, container, false);

        recyclerView = (RecyclerView)view.findViewById(R.id.seller_menu_recyclerView);

        foodTruckInfo = (FoodTruckInfo)getArguments().getSerializable("foodTruckInfo");
        menuList = foodTruckInfo.getMenuList();

        recyclerView.setHasFixedSize(true);
        adapter = new SellerMenuListAdapter(getActivity(),menuList,foodTruckInfo);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        FloatingActionButton sellerMenuFloatingBtn = (FloatingActionButton)view.findViewById(R.id.sellerMenuFloatingBtn);
        sellerMenuFloatingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), SellerMenuEditActivity.class);
                intent.putExtra("foodTruckInfo",foodTruckInfo);
                intent.putExtra("serverURL",serverURL);
                getActivity().startActivityForResult(intent,100);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK) {
            Log.i("Edit","Fragment - UI 갱신");
            Log.i("Edit","foodTruckInfo.getMenuList().size()"+foodTruckInfo.getMenuList().size());
            adapter.notifyDataSetChanged();
        }
    }

    public SellerMenuListAdapter getAdapter() {
        return adapter;
    }
}
