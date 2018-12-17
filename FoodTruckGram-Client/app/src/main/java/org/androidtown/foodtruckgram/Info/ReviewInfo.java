package org.androidtown.foodtruckgram.Info;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by user on 2018-09-13.
 */

public class ReviewInfo implements Serializable {

    private String userName;
    private Date date;
    private String review;

    public ReviewInfo() {

    }

    public ReviewInfo(String userName, Date date, String review) {
        this.userName = userName;
        this.date = date;
        this.review = review;

    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
