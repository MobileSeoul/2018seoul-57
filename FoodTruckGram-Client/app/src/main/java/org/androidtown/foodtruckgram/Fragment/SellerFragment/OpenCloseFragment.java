package org.androidtown.foodtruckgram.Fragment.SellerFragment;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PointF;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import org.androidtown.foodtruckgram.Activity.FoodTruckLocationActivity;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class OpenCloseFragment extends Fragment {

    View view;

    RelativeLayout mapLayout;
    TMapView tMapView;
    TMapMarkerItem tItem;
    String apiKey = "1c8d9930-5a2b-4ed2-8c32-fa7d9a216834";
    double longitude,latitude;
    FoodTruckInfo foodTruckInfo;

    public OpenCloseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_open_close, container, false);

        foodTruckInfo = (FoodTruckInfo)getArguments().getSerializable("foodTruckInfo");

        mapLayout = (RelativeLayout) view.findViewById(R.id.sellerTmapFragment);
        tMapView = new TMapView(getActivity());
        tItem = new TMapMarkerItem();

        //세팅
        tMapView.setSKTMapApiKey(apiKey);
        /*tMapView.setLocationPoint(126.970325,37.556152);
        tMapView.setCenterPoint(126.970325,37.556152);*/
        tMapView.setCompassMode(false);
        tMapView.setIconVisibility(false);
        tMapView.setZoomLevel(16);
        tMapView.setMapType(TMapView.MAPTYPE_STANDARD);  //일반지도
        tMapView.setLanguage(TMapView.LANGUAGE_KOREAN);
        tMapView.setTrackingMode(false);
        tMapView.setSightVisible(false);

        tMapView.setCenterPoint(126.97794509999994,37.5662952);
        tMapView.setLocationPoint(126.97794509999994,37.5662952); //서울시청

        mapLayout.addView(tMapView);

        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback() {
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                tMapView.setCenterPoint(tMapPoint.getLongitude(),tMapPoint.getLatitude());
                latitude = pointF.x;
                longitude = pointF.y;
                Log.i("Tmap","longitude = "+longitude+"latitude = "+latitude);

                tItem.setTMapPoint(tMapPoint);
                tItem.setName("오픈위치");
                tItem.setVisible(TMapMarkerItem.VISIBLE);

                Bitmap bitmap = BitmapFactory.decodeResource(getContext().getResources(),R.drawable.marker);
                tItem.setIcon(bitmap);

                // 핀모양으로 된 마커를 사용할 경우 마커 중심을 하단 핀 끝으로 설정.
                tItem.setPosition((float) 0.5,(float)1.0);     // 마커의 중심점을 하단, 중앙으로 설정

                tMapView.addMarkerItem("openLocation",tItem);
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });

        final ImageView sellerCurrentLocationBtn = (ImageView)view.findViewById(R.id.sellerCurrentLocationBtn);
        sellerCurrentLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Animation anim = AnimationUtils.loadAnimation(
                        view.getContext().getApplicationContext(), // 현재 화면의 제어권자
                        R.anim.rotate_anim);    // 설정한 에니메이션 파일
                sellerCurrentLocationBtn.startAnimation(anim);
                setCurrentLocation();
            }
        });
        final TextView textView = (TextView)view.findViewById(R.id.seller_openPage_textView);
        final TextView openBtn = (TextView)view.findViewById(R.id.openBtn);
        final TextView closeBtn = (TextView)view.findViewById(R.id.closeBtn);

        if(foodTruckInfo.isOpen().equals("false")){
            //Open State
            textView.setText("현재 위치에서 개점하시겠습니까?");
            openBtn.setVisibility(View.VISIBLE);
            closeBtn.setVisibility(View.INVISIBLE);
        }
        else{
            textView.setText("폐점하시겠습니까?");
            openBtn.setVisibility(View.INVISIBLE);
            closeBtn.setVisibility(View.VISIBLE);
        }

        openBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("폐점하시겠습니까?");
                openBtn.setVisibility(View.INVISIBLE);
                closeBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"개점 완료되었습니다. 오늘 하루도 수고하세요^^",Toast.LENGTH_LONG).show();

                foodTruckInfo.opening(longitude,latitude);

                Log.i("OpenClose","longitude = "+longitude+"latitude = "+latitude);
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText("현재 위치에서 개점하시겠습니까?");
                closeBtn.setVisibility(View.INVISIBLE);
                openBtn.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(),"폐점 완료되었습니다. 수고하셨습니다^^",Toast.LENGTH_LONG).show();

                foodTruckInfo.closing();
            }
        });

        return view;
    }

    public void setCurrentLocation() {
        final LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            tMapView.setLocationPoint(longitude, latitude);
            tMapView.setCenterPoint(longitude, latitude);
        } else {
            Toast.makeText(getContext(),"현재위치를 가져오지 못하였습니다.\n직접 지도를 움직여 선택하는 것도 가능합니다.",Toast.LENGTH_SHORT).show();
        }
    }


}
