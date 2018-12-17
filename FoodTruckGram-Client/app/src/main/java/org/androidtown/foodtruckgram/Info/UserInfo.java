package org.androidtown.foodtruckgram.Info;

import java.util.ArrayList;

import java.io.Serializable;

/**
 * Created by 이예지 on 2018-09-06.
 */

public class UserInfo implements Serializable {

    private static UserInfo userInfo = new UserInfo();
    private String userId, password, userName, role, tel;
    private ArrayList<FoodTruckInfo> myFavoriteList;

    private UserInfo(){

    }

    public static UserInfo getUserInfo(){
        return userInfo;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public ArrayList<FoodTruckInfo> getMyFavoriteList() {
        return myFavoriteList;
    }

    public void setMyFavoriteList(ArrayList<FoodTruckInfo> myFavoriteList) {
        this.myFavoriteList = myFavoriteList;
    }

    public void addFavoriteTruck(FoodTruckInfo foodTruckInfo){
        myFavoriteList.add(foodTruckInfo);
    }

    public void removeFavoriteTruck(FoodTruckInfo foodTruckInfo){
        myFavoriteList.remove(foodTruckInfo);
    }

}
