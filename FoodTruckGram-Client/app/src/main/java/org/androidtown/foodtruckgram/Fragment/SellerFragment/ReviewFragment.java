package org.androidtown.foodtruckgram.Fragment.SellerFragment;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidtown.foodtruckgram.Adapter.SellerReviewListAdapter;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.ReviewInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReviewFragment extends Fragment {

    private RecyclerView recyclerView;
    private SellerReviewListAdapter adapter;
    private TextView review_count;

    String serverURL_review = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/getReview";

    public ReviewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_review, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.seller_review);
        review_count = (TextView) view.findViewById(R.id.review_count);

        return view;

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        FoodTruckInfo foodTruckInfo = (FoodTruckInfo) getArguments().getSerializable("foodTruckInfo");

        ReviewDB reviewDB = new ReviewDB();
        Map<String, String> param = new HashMap<>();
        param.put("storeName", foodTruckInfo.getStoreName());
        reviewDB.execute(param);
    }

    class ReviewDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_review);
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

            ArrayList<ReviewInfo> infos = gson.fromJson(aVoid, new TypeToken<List<ReviewInfo>>() {
            }.getType());


            recyclerView.setHasFixedSize(true);
            adapter = new SellerReviewListAdapter(getActivity(), infos, review_count);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            recyclerView.setAdapter(adapter);

            review_count.setText(Integer.toString(adapter.getItemCount()));


    }

}

}
