package org.androidtown.foodtruckgram.Fragment;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ListView;

import org.androidtown.foodtruckgram.Activity.AddReviewDialog;
import org.androidtown.foodtruckgram.Adapter.CustomerReviewAdapter;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.ReviewInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class CustomerReviewFragment extends Fragment {

    private View view;
    private ListView reviewListView = null;
    private FoodTruckInfo foodTruckInfo;
    private FloatingActionButton addReviewBtn;
    private ArrayList<ReviewInfo> reviewInfos;

    private CustomerReviewAdapter reviewAdapter;

    private AddReviewDialog reviewDialog;
    UserInfo userInfo = UserInfo.getUserInfo();

    public CustomerReviewFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_customer_review, container, false);



        addReviewBtn = (FloatingActionButton) view.findViewById(R.id.fab_review_add);
        addReviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                reviewDialog.show();
            }
        });

        Bundle bundle = getArguments();

        foodTruckInfo = (FoodTruckInfo) bundle.getSerializable("foodtruckInfo");
        reviewInfos = (ArrayList<ReviewInfo>) bundle.getSerializable("reviewInfos");

        //리뷰작성 다이얼로그
        DisplayMetrics dm = view.getContext().getApplicationContext().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        int width = dm.widthPixels; //디바이스 화면 너비
        int height = dm.heightPixels; //디바이스 화면 높이

        reviewDialog = new AddReviewDialog(view.getContext(),foodTruckInfo, reviewInfos);

        WindowManager.LayoutParams wm = reviewDialog.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(reviewDialog.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width =  (width/3)*2;  //화면 너비의 절반
        wm.height = height / 2;  //화면 높이의 절반


        if(reviewInfos!=null) {

            // ListView, Adapter 생성 및 연결 ------------------------
            reviewListView = (ListView) view.findViewById(R.id.review_listview);
            reviewAdapter = new CustomerReviewAdapter(foodTruckInfo, userInfo, reviewInfos);
            reviewListView.setAdapter(reviewAdapter);

        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        reviewAdapter.notifyDataSetChanged();
        Log.i("datasetChange","change");
    }
}
