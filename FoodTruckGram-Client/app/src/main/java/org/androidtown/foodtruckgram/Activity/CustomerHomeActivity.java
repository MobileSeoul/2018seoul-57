package org.androidtown.foodtruckgram.Activity;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.androidtown.foodtruckgram.Adapter.CustomerMyOrderAdapter;
import org.androidtown.foodtruckgram.Adapter.ViewPagerAdapter;
import org.androidtown.foodtruckgram.Fragment.HomeFragment;
import org.androidtown.foodtruckgram.Fragment.MapFragment;
import org.androidtown.foodtruckgram.Fragment.MyOrderFragment;
import org.androidtown.foodtruckgram.Fragment.TruckListFragment;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.Info.OrderInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerHomeActivity extends AppCompatActivity {

    ActionBar actionBar;

    private ViewPager viewPager;
    private static ViewPagerAdapter viewPagerAdapter;

    BottomNavigationView bottomNavigationView;

    HomeFragment homeFragment;
    //FavoriteFragment favoriteFragment;
    MapFragment mapFramgment;
    MyOrderFragment myOrderFragment;
    TruckListFragment truckListFragment;

    int currentMenu;
    MenuItem prevMenuItem;

    UserInfo userInfo = UserInfo.getUserInfo();
    List<FoodTruckInfo> foodTruckInfos;
    String serverURL_getFoodTruckInfoList = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/getFoodTruckInfoList";
    String serverURL_getFavoriteTruck = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/getFavoriteStoreByUserId";
    FoodTruckDB foodTruckDB;
    FavoriteTruckDB favoriteTruckDB;

    static final int SMS_READPHONE_PERMISSON=1;

    BroadcastReceiver myReceiver = new SMSReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        setContentView(R.layout.activity_customer_home);

        foodTruckDB = new FoodTruckDB();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userInfo.getUserId());
        foodTruckDB.execute(params);

        favoriteTruckDB = new FavoriteTruckDB();
        favoriteTruckDB.execute(params);


        //권한이 부여되어 있는지 확인
        int permissonCheckRead= ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if(permissonCheckRead == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(getApplicationContext(), "SMS read권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SMS read권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_PHONE_STATE)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_PHONE_STATE}, SMS_READPHONE_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.READ_PHONE_STATE}, SMS_READPHONE_PERMISSON);
            }
        }



        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);


        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");


        registerReceiver(myReceiver, intentFilter);

        Log.d("onCreate()","브로드캐스트리시버 등록됨");

    }

    @Override

    protected void onDestroy() {

        super.onDestroy();

        unregisterReceiver(myReceiver);

        Log.d("onDestory()","브로드캐스트리시버 해제됨");

    }


    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 4);

        homeFragment = new HomeFragment();
        mapFramgment = new MapFragment();
        truckListFragment = new TruckListFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("foodTruckInfos",(Serializable) foodTruckInfos);
        truckListFragment.setArguments(bundle);
        mapFramgment.setArguments(bundle);

        myOrderFragment = new MyOrderFragment();
        myOrderFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(homeFragment);
        viewPagerAdapter.addFragment(mapFramgment);
        viewPagerAdapter.addFragment(truckListFragment);
        viewPagerAdapter.addFragment(myOrderFragment);

        viewPager.setAdapter(viewPagerAdapter);
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

            foodTruckInfos = info;

            viewPager = (ViewPager) findViewById(R.id.viewPager);
            viewPager.setOffscreenPageLimit(4);

            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);

            currentMenu = R.id.navigation_home;
            setupViewPager(viewPager);
            prevMenuItem = bottomNavigationView.getMenu().getItem(0);


            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            currentMenu = R.id.navigation_home;
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_map:
                            currentMenu = R.id.navigation_map;
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.navigation_list:
                            currentMenu = R.id.navigation_list;
                            viewPager.setCurrentItem(2);
                            ////foodTruckInfos = info;
                            Bundle bundle = new Bundle();
                            bundle.putSerializable("foodtruckINFO", (Serializable) foodTruckInfos);
                            truckListFragment.setArguments(bundle);
                            break;
                        case R.id.navigation_order:
                            currentMenu = R.id.navigation_order;
                            viewPager.setCurrentItem(3);
                            break;
                    }
                    return true;
                }
            });


            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    currentMenu = bottomNavigationView.getMenu().getItem(position).getItemId();
                    bottomNavigationView.getMenu().getItem(position).setChecked(true);
                    prevMenuItem = bottomNavigationView.getMenu().getItem(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        }
    }

    class FavoriteTruckDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_getFavoriteTruck);
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

            ArrayList<FoodTruckInfo> info = gson.fromJson(aVoid, new TypeToken<List<FoodTruckInfo>>(){}.getType());
            UserInfo.getUserInfo().setMyFavoriteList(info);
        }
    }


}
