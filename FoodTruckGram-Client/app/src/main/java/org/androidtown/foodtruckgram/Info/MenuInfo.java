package org.androidtown.foodtruckgram.Info;

import java.io.Serializable;

/**
 * Created by 이예지 on 2018-09-11.
 */

public class MenuInfo implements Serializable {

    private String storeName;
    private String menuImage;
    private String menuName;
    private String menuPrice;
    private String menuIntroduce;

    public MenuInfo(){

    }

    public MenuInfo(String menuName, String menuPrice, String menuImage){
        setMenuName(menuName);
        setMenuPrice(menuPrice);
        setMenuImage(menuImage);
    }

    public MenuInfo(String menuName, String menuPrice, String menuImage,String menuIntroduce){
        setMenuName(menuName);
        setMenuPrice(menuPrice);
        setMenuImage(menuImage);
        setMenuIntroduce(menuIntroduce);
    }


    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getMenuImage() {
        return menuImage;
    }

    public void setMenuImage(String menuImage) {
        this.menuImage = menuImage;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuPrice() {
        return menuPrice;
    }

    public void setMenuPrice(String menuPrice) {
        this.menuPrice = menuPrice;
    }

    public String getMenuIntroduce() {
        return menuIntroduce;
    }

    public void setMenuIntroduce(String menuIntroduce) {
        this.menuIntroduce = menuIntroduce;
    }
}
