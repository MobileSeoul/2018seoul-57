package org.androidtown.foodtruckgram.Fragment;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidtown.foodtruckgram.Activity.CustomerHomeActivity;
import org.androidtown.foodtruckgram.Adapter.CustomerMyOrderAdapter;
import org.androidtown.foodtruckgram.Adapter.CustomerOrderMenuAdapter;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.OrderInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.app.Activity.RESULT_OK;


public class MyOrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private static CustomerMyOrderAdapter adapter;
    private TextView myorder_count;
    private List<FoodTruckInfo> foodTruckInfos;

    String serverURL_myorder = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/getOrderListByUserId";

    private View view;

    public MyOrderFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_my_order, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.customer_myorder);
        myorder_count = (TextView) view.findViewById(R.id.myorder_count);

        Bundle bundle = getArguments();
        foodTruckInfos = (ArrayList<FoodTruckInfo>) bundle.getSerializable("foodTruckInfos");

        Log.i("Order","Fragment onCreateView");
        OrderDB orderDB = new OrderDB();
        Map<String, String> param = new HashMap<>();
        param.put("userId", UserInfo.getUserInfo().getUserId());
        orderDB.execute(param);

        final ImageView myOrderListRefreshBtn = (ImageView)view.findViewById(R.id.myOrderListRefreshBtn);
        myOrderListRefreshBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateUI();
                Animation anim = AnimationUtils.loadAnimation(
                        view.getContext().getApplicationContext(), // 현재 화면의 제어권자
                        R.anim.rotate_anim);    // 설정한 에니메이션 파일
                myOrderListRefreshBtn.startAnimation(anim);

            }
        });

        return view;
    }

    public static CustomerMyOrderAdapter getAdapter() {
        return adapter;
    }

    public void updateUI(){
        OrderDB orderDB = new OrderDB();
        Map<String, String> param = new HashMap<>();
        param.put("userId", UserInfo.getUserInfo().getUserId());
        orderDB.execute(param);
    }


    class OrderDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_myorder);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            //Log.i(TAG, "응답코드"+statusCode);

            String body = post.getBody();

            //Log.i(TAG, "body : "+body);

            return body;

        }


        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

            Gson gson = new Gson();

            ArrayList<OrderInfo> infos = gson.fromJson(aVoid, new TypeToken<List<OrderInfo>>() {
            }.getType());

//            Log.i("Order","OrderSize : "+infos.size());

            if(infos != null) {
                Log.i("Order","OrderSize : "+infos.size());
                //recyclerView.setHasFixedSize(true);
                adapter = new CustomerMyOrderAdapter(getActivity(), infos, myorder_count);
                adapter.notifyDataSetChanged();
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                recyclerView.setAdapter(adapter);

                myorder_count.setText(Integer.toString(adapter.getItemCount()));
            }



        }
    }

}
