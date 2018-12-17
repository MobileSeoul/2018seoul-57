package org.androidtown.foodtruckgram.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapGpsManager;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.R;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.LogManager;

import javax.xml.parsers.ParserConfigurationException;

public class FoodTruckLocationActivity extends AppCompatActivity {

    private FoodTruckInfo foodTruckInfo;

    private LinearLayout mapLayout;
    private TMapGpsManager gps;
    private TMapView tMapView;
    private TMapData tmapdata = new TMapData();

    private static int mMarkerID;
    private static String apiKey = "1c8d9930-5a2b-4ed2-8c32-fa7d9a216834";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_truck_location);

        Intent intent = getIntent();
        foodTruckInfo = (FoodTruckInfo) intent.getSerializableExtra("FoodTruckInfo");

        //권한
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1); //위치권한 탐색 허용 관련 내용
            }
        }

        tMapView = new TMapView(this);

        //세팅
        tMapView.setSKTMapApiKey(apiKey);
        tMapView.setCompassMode(false);
        tMapView.setIconVisibility(true);
        tMapView.setZoomLevel(15);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);  //일반지도
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setTrackingMode(true);
        tMapView.setSightVisible(true);

//        gps = new TMapGpsManager(this);

        //map view
        mapLayout = (LinearLayout) findViewById(R.id.foodtruck_location_layout);
        mapLayout.addView(tMapView);

        markerFoodtruck();

    }


    public void markerFoodtruck() {

        // 마커 아이콘
        Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.marker);

        double foodturckLatitude = foodTruckInfo.getLatitude();
        double foodturckLongitude = foodTruckInfo.getLongitude();
        double userLatitude;
        double userLongitude;

        TMapPoint tMapPoint = new TMapPoint(foodturckLatitude, foodturckLongitude); //푸드트럭 위치 저장
        TMapMarkerItem tMapMarkerItem = new TMapMarkerItem();

        tMapMarkerItem.setIcon(bitmap); // 마커 아이콘 지정
        tMapMarkerItem.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        tMapMarkerItem.setTMapPoint(tMapPoint); // 마커의 좌표 지정
        tMapMarkerItem.setName(foodTruckInfo.getStoreName()); // 마커의 타이틀 지정
        tMapMarkerItem.setVisible(TMapMarkerItem.VISIBLE);
        tMapMarkerItem.setCanShowCallout(true); //풍선뷰 설정
        tMapMarkerItem.setCalloutTitle(tMapMarkerItem.getName()); //풍선뷰에 표시될 내용

        tMapView.addMarkerItem("markerItem", tMapMarkerItem); // 지도에 마커 추가

        // tMapView.setLocationPoint(foodturckLongitude, foodturckLatitude);
        // tMapView.setIconVisibility(false);
        tMapView.setCenterPoint(foodturckLongitude, foodturckLatitude);

        gpsToAddress(foodturckLatitude, foodturckLongitude);


        //user 위치
        final LocationManager lm = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            userLatitude = location.getLatitude();
            userLongitude = location.getLongitude();

            tMapView.setLocationPoint(userLongitude, userLatitude);
            tMapView.setIconVisibility(true);

            TMapPoint tMapPointStart = new TMapPoint(userLatitude, userLongitude); // 현재위치 --> 출발지
            TMapPoint tMapPointEnd = new TMapPoint(foodturckLatitude, foodturckLongitude); // 푸드트럭 위치 --> 도착지

            findPath(tMapPointStart, tMapPointEnd);
        } else {
            Toast.makeText(FoodTruckLocationActivity.this, "현재위치를 가져오지 못하였습니다.", Toast.LENGTH_SHORT).show();
            tMapView.setLocationPoint(foodturckLongitude, foodturckLatitude); //푸드트럭 위치만 찍기
        }


    }

    //경로 안내
    public void findPath(TMapPoint tMapPointStart, TMapPoint tMapPointEnd) {

        tmapdata.findPathDataWithType(TMapData.TMapPathType.CAR_PATH, tMapPointStart, tMapPointEnd, new TMapData.FindPathDataListenerCallback() {
            @Override
            public void onFindPathData(TMapPolyLine polyLine) {

                //폴리 라인을 그리고 시작, 끝지점의 거리를 산출하여 로그에 표시한다
                tMapView.addTMapPath(polyLine);
                double wayDistance = polyLine.getDistance();
                Log.i("Distance", wayDistance + " ");
            }
        });
    }

    String address;

    public void gpsToAddress(double foodturckLatitude, double foodturckLongitude) {
        //푸드트럭 위치 주소로 나타내기


        tmapdata.convertGpsToAddress(foodturckLatitude, foodturckLongitude, new TMapData.ConvertGPSToAddressListenerCallback() {

            @Override
            public void onConvertToGPSToAddress(String s) {
                Log.i("toAddress", "주소 : " + s);
                address = s.toString();
                ActionBar actionBar = getSupportActionBar();
                assert actionBar != null;
                actionBar.setTitle(address);
            }
        });
    }


}
