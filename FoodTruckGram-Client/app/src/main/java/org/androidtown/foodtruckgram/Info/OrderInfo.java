package org.androidtown.foodtruckgram.Info;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by 이예지 on 2018-09-11.
 */

public class OrderInfo implements Serializable {
    private String tel,storeName,userId, menuName;
    private int price;
    private List<MenuInfo> orderMenuInfos;
    private Date date;

    /*public void addMenu(MenuInfo newMenu){
        orderMenuList.add(newMenu);
    }

    public void removeMenu(MenuInfo menu){
        //품절 메뉴 설정
        for(int i=0;i<orderMenuList.size();i++){
            if(orderMenuList.get(i).equals(menu))
                orderMenuList.remove(i);
        }
    }*/


    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    /* public ArrayList<MenuInfo> getOrderMenuList() {
        return orderMenuList;
    }

    public void setOrderMenuList(ArrayList<MenuInfo> orderMenuList) {
        this.orderMenuList = orderMenuList;
    }*/

    public List<MenuInfo> getOrderMenuInfos() {
        return orderMenuInfos;
    }

    public void setOrderMenuInfos(List<MenuInfo> orderMenuInfos) {
        this.orderMenuInfos = orderMenuInfos;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }
}
