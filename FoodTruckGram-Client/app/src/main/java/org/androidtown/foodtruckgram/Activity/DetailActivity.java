package org.androidtown.foodtruckgram.Activity;

import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidtown.foodtruckgram.Adapter.CustomerMyOrderAdapter;
import org.androidtown.foodtruckgram.Adapter.ViewPagerAdapter;
import org.androidtown.foodtruckgram.Fragment.CustomerReviewFragment;
import org.androidtown.foodtruckgram.Fragment.MyOrderFragment;
import org.androidtown.foodtruckgram.Fragment.OrderFragment;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.OrderInfo;
import org.androidtown.foodtruckgram.Info.ReviewInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetailActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    private CustomerReviewFragment reviewFragment = new CustomerReviewFragment();
    private OrderFragment orderFragment = new OrderFragment();

    private int currentMenu;
    private MenuItem prevMenuItem;

    private UserInfo userInfo = UserInfo.getUserInfo();
    private List<FoodTruckInfo> foodTruckInfos;
    String serverURL_getFoodTruckInfoList = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/getFoodTruckInfoList";
    private List<ReviewInfo> reviewInfos;
    private String serverURL_geReviewInfoList = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/getReview";
    private FoodTruckDB foodTruckDB;
    private FoodTruckReviewDB foodTruckReviewDB;

    private TabLayout tabLayout;
    private ActionBar actionBar;
    FoodTruckInfo foodTruckInfo;

    private TextView foodtruckName;
    private ImageButton foodtruckLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
/*

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.hideOverflowMenu();
*/

        actionBar = getSupportActionBar();
        actionBar.hide();

        Intent intent = getIntent();
        foodTruckInfo = (FoodTruckInfo) intent.getSerializableExtra("foodtruckInfo");

        Map<String, String> params = new HashMap<String, String>();
        params.put("storeName", foodTruckInfo.getStoreName());

        foodtruckName = (TextView) findViewById(R.id.foodtruck_detail_name);
        foodtruckName.setText(foodTruckInfo.getStoreName());

        foodTruckReviewDB = new FoodTruckReviewDB();
        foodTruckReviewDB.execute(params);

        foodTruckDB = new FoodTruckDB();
        foodTruckDB.execute(params);

        foodtruckLocation = (ImageButton) findViewById(R.id.foodtruck_detail_location);

        foodtruckLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /////////////지도에 푸드트럭 위치 찍어주기!
                Intent intent = new Intent(getApplicationContext(), FoodTruckLocationActivity.class);
                intent.putExtra("FoodTruckInfo",foodTruckInfo);
                startActivity(intent);
            }
        });

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 2);

        Bundle bundle = new Bundle();
        bundle.putSerializable("foodtruckInfo", foodTruckInfo);
        bundle.putSerializable("reviewInfos", (Serializable) reviewInfos);


        reviewFragment.setArguments(bundle);
        orderFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(orderFragment);
        viewPagerAdapter.addFragment(reviewFragment);

        viewPager.setAdapter(viewPagerAdapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

    }

    class FoodTruckDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_getFoodTruckInfoList);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드"+statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : "+body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            Gson gson = new Gson();

            List<FoodTruckInfo> info = gson.fromJson(aVoid, new TypeToken<List<FoodTruckInfo>>(){}.getType());

            foodTruckInfos = info; //푸드트럭 인포 받아옴

            tabLayout = (TabLayout)findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText("주문하기"));
            tabLayout.addTab(tabLayout.newTab().setText("리뷰작성"));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

            tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.colorAccent));
            tabLayout.setTabTextColors(R.color.white,getResources().getColor(R.color.black));


            viewPager = (ViewPager) findViewById(R.id.detail_viewpager);
            viewPager.setOffscreenPageLimit(2);


            currentMenu = R.id.navigation_home;
            setupViewPager(viewPager);

            // Set TabSelectedListener
            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });
        }
    }


    class FoodTruckReviewDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_geReviewInfoList);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드"+statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : "+body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            Gson gson = new Gson();

            List<ReviewInfo> infos = gson.fromJson(aVoid, new TypeToken<List<ReviewInfo>>(){}.getType());

            reviewInfos = infos; //푸드트럭 인포 받아옴

        }

    }
}
