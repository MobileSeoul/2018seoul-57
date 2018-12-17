package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.ReviewInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Haneul on 2018-09-19.
 */

public class CustomerReviewAdapter extends BaseAdapter{

    LayoutInflater inflater = null;
    UserInfo userInfo = UserInfo.getUserInfo();
    //private int listCount = 0;
    private FoodTruckInfo foodTruckInfo = null;
    private ArrayList<ReviewInfo> reviewInfos;

    private ArrayList<FoodTruckInfo> foodTruckInfos;

    public CustomerReviewAdapter(FoodTruckInfo foodTruckInfo, UserInfo userInfo, ArrayList<ReviewInfo> reviewInfos) {
        this.foodTruckInfo = foodTruckInfo;
        this.userInfo = userInfo;
        this.reviewInfos = reviewInfos;


    }


    @Override
    public int getCount() {
        if(reviewInfos!=null)
            return reviewInfos.size();
        else{
            Log.i("Review","reviewInfos null");
            return 0;
        }
    }

    @Override
    public Object getItem(int i) {
        return reviewInfos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.customer_review_item, parent, false);
        }



        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        ImageView userImage = (ImageView) convertView.findViewById(R.id.user_img);
        TextView userID = (TextView) convertView.findViewById(R.id.user_id);
        TextView reviewContent = (TextView) convertView.findViewById(R.id.review_content);
        TextView reviewDate = (TextView) convertView.findViewById(R.id.review_date);

       // userImage.setImageResource(R.drawable.foodtruckgram); //프로필 사진
        userID.setText(reviewInfos.get(position).getUserName());
        reviewContent.setText(reviewInfos.get(position).getReview());
        String reviewdate = sdf.format(reviewInfos.get(position).getDate());
        reviewDate.setText(reviewdate);


        return convertView;
    }
}
