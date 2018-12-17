package org.androidtown.foodtruckgram.Activity;

import android.Manifest;
import android.app.Fragment;
import android.content.Intent;
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

import org.androidtown.foodtruckgram.Adapter.SellerMenuListAdapter;
import org.androidtown.foodtruckgram.Adapter.ViewPagerAdapter;
import org.androidtown.foodtruckgram.Fragment.SellerFragment.MenuFragment;
import org.androidtown.foodtruckgram.Fragment.SellerFragment.OpenCloseFragment;
import org.androidtown.foodtruckgram.Fragment.SellerFragment.OrderListFragment;
import org.androidtown.foodtruckgram.Fragment.SellerFragment.ReviewFragment;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerHomeActivity extends AppCompatActivity {

    private final String TAG = "SellerHomeActivity";
    private ViewPager viewPager;
    private ViewPagerAdapter viewPagerAdapter;

    BottomNavigationView bottomNavigationView;

    OpenCloseFragment openCloseFragment;
    OrderListFragment orderListFragment;
    MenuFragment menuFragment;
    ReviewFragment reviewFragment;

    int currentMenu;
    MenuItem prevMenuItem;
    UserInfo userInfo = UserInfo.getUserInfo();
    public FoodTruckInfo foodTruckInfo;

    String serverURL_getFoodTruckInfo = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/getFoodTruckInfoByStoreName";
    FoodTruckDB  foodTruckDB;

    static final int SMS_SEND_PERMISSON=1;
    static final int SMS_READPHONE_PERMISSON=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setContentView(R.layout.activity_seller_home);

        foodTruckDB = new FoodTruckDB();
        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userInfo.getUserId());
        foodTruckDB.execute(params);

        //권한이 부여되어 있는지 확인
        int permissonCheck= ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if(permissonCheck == PackageManager.PERMISSION_GRANTED){
            //Toast.makeText(getApplicationContext(), "SMS 송신권한 있음", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(getApplicationContext(), "SMS 송신권한 없음", Toast.LENGTH_SHORT).show();

            //권한설정 dialog에서 거부를 누르면
            //ActivityCompat.shouldShowRequestPermissionRationale 메소드의 반환값이 true가 된다.
            //단, 사용자가 "Don't ask again"을 체크한 경우
            //거부하더라도 false를 반환하여, 직접 사용자가 권한을 부여하지 않는 이상, 권한을 요청할 수 없게 된다.
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                //이곳에 권한이 왜 필요한지 설명하는 Toast나 dialog를 띄워준 후, 다시 권한을 요청한다.
                Toast.makeText(getApplicationContext(), "SMS권한이 필요합니다", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSON);
            }else{
                ActivityCompat.requestPermissions(this, new String[]{ Manifest.permission.SEND_SMS}, SMS_SEND_PERMISSON);
            }
        }

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

    }

    private void setupViewPager(ViewPager viewPager) {
        viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 4);

        Bundle bundle = new Bundle(1);
        bundle.putSerializable("foodTruckInfo",foodTruckInfo);

        openCloseFragment = new OpenCloseFragment();
        openCloseFragment.setArguments(bundle);

        orderListFragment = new OrderListFragment();
        orderListFragment.setArguments(bundle);

        menuFragment = new MenuFragment();
        menuFragment.setArguments(bundle);

        reviewFragment = new ReviewFragment();
        reviewFragment.setArguments(bundle);

        viewPagerAdapter.addFragment(openCloseFragment); // home
        viewPagerAdapter.addFragment(orderListFragment);
        viewPagerAdapter.addFragment(menuFragment);
        viewPagerAdapter.addFragment(reviewFragment);

        viewPager.setAdapter(viewPagerAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode==RESULT_OK) {
            FoodTruckUpdateDB foodTruckDB = new FoodTruckUpdateDB();
            Map<String, String> params = new HashMap<String, String>();
            params.put("userId", userInfo.getUserId());
            foodTruckDB.execute(params);

            Log.i("Edit","Activity - UI 갱신");
            Log.i("Edit","foodTruckInfo.getMenuList().size()"+foodTruckInfo.getMenuList().size());

        }
    }

    class FoodTruckUpdateDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_getFoodTruckInfo);
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

            FoodTruckInfo info = gson.fromJson(aVoid, FoodTruckInfo.class);
            foodTruckInfo = info;

            SellerMenuListAdapter adapter = menuFragment.getAdapter();
            adapter.notifyData(foodTruckInfo);
            viewPagerAdapter.switchFragment(2,new MenuFragment());
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.seller_menu_recyclerView);
            recyclerView.setAdapter(adapter);
        }
    }

    class FoodTruckDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_getFoodTruckInfo);
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

            FoodTruckInfo info = gson.fromJson(aVoid, FoodTruckInfo.class);
            foodTruckInfo = info;

            List<MenuInfo> menuInfos = info.getMenuList();

            for(int i=0; i<menuInfos.size(); i++) {
                String base64 = menuInfos.get(i).getMenuImage();
                /*byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                menuInfos.get(i).setMenuBitmap(bitmap);*/
            }

           // Log.i("yunjae", "storeName = " + foodTruckInfo.getStoreName() + " ownerName = " + foodTruckInfo.getOwnerName() + " ownerId = " + foodTruckInfo.getOwnerId() + " menuList0 = " + foodTruckInfo.getMenuList().get(0).getMenuName());


            viewPager = (ViewPager) findViewById(R.id.viewPager_seller);
            viewPager.setOffscreenPageLimit(4);

            bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation_seller);

            currentMenu = R.id.navigation_open;
            setupViewPager(viewPager);
            prevMenuItem = bottomNavigationView.getMenu().getItem(0);

            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {

                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_open:
                            currentMenu = R.id.navigation_open;
                            viewPager.setCurrentItem(0);
                            break;
                        case R.id.navigation_order_seller:
                            currentMenu = R.id.navigation_order_seller;;
                            viewPager.setCurrentItem(1);
                            break;
                        case R.id.navigation_menu:
                            currentMenu = R.id.navigation_menu;
                            viewPager.setCurrentItem(2);
                            break;
                        case R.id.navigation_review:
                            currentMenu = R.id.navigation_review;
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

}
