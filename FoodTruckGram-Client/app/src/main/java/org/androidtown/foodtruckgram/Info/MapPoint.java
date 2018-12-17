package org.androidtown.foodtruckgram.Info;

import com.skt.Tmap.TMapPoint;

/**
 * Created by Haneul on 2018-09-12.
 */

public class MapPoint {

    private String name;
    private String address;
    private TMapPoint point;

    public MapPoint() {
        super();
    }

    public MapPoint(String name, String address, TMapPoint point) {
        this.name = name;
        this.address = address;
        this.point = point;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public TMapPoint getPoint() {
        return point;
    }

    public void setPoint(TMapPoint point) {
        this.point = point;
    }
}