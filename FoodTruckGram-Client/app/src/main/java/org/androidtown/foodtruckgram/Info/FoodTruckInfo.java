package org.androidtown.foodtruckgram.Info;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

import org.androidtown.foodtruckgram.Server.HttpClient;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 이예지 on 2018-09-11.
 */

public class FoodTruckInfo implements Serializable {

    private String storeName="", ownerId="", ownerName="";
    private double longitude,latitude;
    private String isOpen;
    private ArrayList<MenuInfo> menuList;

    String serverURL_openFoodTruck = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/updateFoodTruckLocationAndOpen";
    String serverURL_closeFoodTruck = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/updateFoodTruckClose";



    public void opening(double longitude, double latitude){
        //서버에 개점알림
        this.longitude = longitude;
        this.latitude = latitude;
        this.isOpen = "true";

        FoodTruckOpenDB foodTruckOpenDB = new FoodTruckOpenDB();

        Map<String,String> params = new HashMap<String,String>();
        params.put("storeName",storeName);
        params.put("longitude",longitude+"");
        params.put("latitude",latitude+"");

        foodTruckOpenDB.execute(params);
    }

    public void closing(){
        //서버에 폐점알림
        this.isOpen = "false";

        FoodTruckCloseDB foodTruckCloseDB = new FoodTruckCloseDB();
        Map<String,String> params = new HashMap<String,String>();
        params.put("storeName",storeName);

        foodTruckCloseDB.execute(params);
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void addMenu(MenuInfo newMenu){
        menuList.add(newMenu);
    }

    public void removeMenu(MenuInfo menu){
        //품절 메뉴 설정
        for(int i=0;i<menuList.size();i++){
            if(menuList.get(i).equals(menu))
                menuList.remove(i);
        }
    }

    public int editMenu(MenuInfo menu){
        for(int i=0;i<menuList.size();i++){
            Log.i("Edit","menuList.get(i).getMenuName() : "+menuList.get(i).getMenuName()+"/new = "+menu.getMenuName());
            if(menuList.get(i).getMenuName().equals(menu.getMenuName())) {
                Log.i("Edit","메뉴 수정");
                menuList.remove(i); //기존 메뉴 삭제
                menuList.add(menu); //수정 메뉴 추가
                return 1;
            }
        }

        Log.i("Edit","메뉴 추가전 메뉴 갯수 : "+menuList.size());
        Log.i("Edit","메뉴 추가");
        menuList.add(menu);
        Log.i("Edit","메뉴 추가후 메뉴 갯수 : "+menuList.size());
        return -1;
    }

    public ArrayList<MenuInfo> getMenuList() {
        return menuList;
    }

    public void setMenuList(ArrayList<MenuInfo> menuList) {
        this.menuList = menuList;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String isOpen() {
        return isOpen;
    }

    public void setOpen(String open) {
        isOpen = open;
    }

    class FoodTruckOpenDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_openFoodTruck);
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

        }
    }

    class FoodTruckCloseDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_closeFoodTruck);
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
        }
    }



}
