package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.androidtown.foodtruckgram.Activity.DetailActivity;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haneul on 2018-09-13.
 */

public class FoodTruckListAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<FoodTruckInfo> foodTruckInfos = null;
    private int listCount = 0;
    private int count = 0;
    public Context context;
    View convertView;


    UserInfo userInfo = UserInfo.getUserInfo();
    ImageView foodtruckFavoriteBtn;

    public FoodTruckListAdapter(ArrayList<FoodTruckInfo> foodTruckInfos, Context context) {
        this.foodTruckInfos = foodTruckInfos;
        this.context = context;
        //listCount = foodTruckInfos.size();
    }

    @Override
    public int getCount() {
        Log.i("TAG", "getCount");
       // return listCount;
        return foodTruckInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return foodTruckInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            final Context context = parent.getContext();
            if (inflater == null) {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.foodtruck_list_item, parent, false);
        }
        this.convertView = convertView;

        ImageView foodtruckProfile = (ImageView) convertView.findViewById(R.id.foodtruck_profile);
        final TextView foodtruckName = (TextView) convertView.findViewById(R.id.foodtruck_name);
        TextView foodtruckID = (TextView) convertView.findViewById(R.id.foodtruck_id);
        ImageView foodtruckImg = (ImageView) convertView.findViewById(R.id.foodtruck_img);
        TextView foodtruckComment = (TextView) convertView.findViewById(R.id.foodtruck_comment);
        ImageButton detailBtn = (ImageButton)convertView.findViewById(R.id.detail_page_btn);

        foodtruckProfile.setImageResource(R.drawable.foodtruckgram); //프로필 사진

        foodtruckName.setText(foodTruckInfos.get(position).getStoreName());
        foodtruckID.setText(foodTruckInfos.get(position).getOwnerId());

       // foodtruckImg.setImageResource(R.drawable.sample); //음식사진
        String base64 = null;
        if(foodTruckInfos.get(position).getMenuList().size() > 0) {
            base64 = foodTruckInfos.get(position).getMenuList().get(0).getMenuImage();
        }

        Log.i("Edit22", "base64 = " + base64);
        if(base64 != null && base64 != "") {
//            byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            foodtruckImg.setImageBitmap(bitmap);
//            Log.i("Edit22", "menuImage = " + base64);
            Picasso.with(context).load(base64).into(foodtruckImg);
        }
        else {
            foodtruckImg.setImageResource(R.drawable.burger);
        }
        foodtruckComment.setText("코멘트 추가해야 함");

        Log.i("foodtruckFavorite", foodTruckInfos.get(position).getStoreName() + "// Position=" + position);

        foodtruckFavoriteBtn = (ImageView) convertView.findViewById(R.id.favorite_btn);

        if (isFavoriteTruck(foodTruckInfos.get(position).getStoreName()))
            foodtruckFavoriteBtn.setImageResource(R.drawable.ic_like_red);
        else
            foodtruckFavoriteBtn.setImageResource(R.drawable.ic_like_black);

        foodtruckFavoriteBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                FavoriteDB favoriteDB = new FavoriteDB();

                Map<String, String> params = new HashMap<String, String>();
                params.put("storeName", foodTruckInfos.get(position).getStoreName());
                params.put("userId", userInfo.getUserId());

                favoriteDB.execute(params);
            }
        });


        final View finalConvertView = convertView;

        //detailActivity
        detailBtn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(finalConvertView.getContext(), DetailActivity.class);
                intent.putExtra("foodtruckInfo", foodTruckInfos.get(position));
                finalConvertView.getContext().startActivity(intent);
            }
        });

        return convertView;
    }

    public boolean isFavoriteTruck(String storeName) {
        ArrayList<FoodTruckInfo> favoriteList = userInfo.getMyFavoriteList();
        if (favoriteList != null) {
            for (int i = 0; i < favoriteList.size(); i++) {
                //유저의 favorite목록중 일치하는 것이 있을때
                if (favoriteList.get(i).getStoreName().equals(storeName)) {
                    Log.i("Favorite", "user");
                    return true;
                }
            }
        }
        return false;
    }

    class FavoriteDB extends AsyncTask<Map<String, String>, Integer, String> {

        String serverURL = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/insertFavoriteStore";

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드" + statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : " + body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            Gson gson = new Gson();

            //하트색 변경
            Log.i("FavoriteList", "foodtruckFavoriteBtn.getDrawable() : " + foodtruckFavoriteBtn.getDrawable());

            if (aVoid.contains("NO")) {
                //관심 취소
                foodtruckFavoriteBtn.setImageResource(R.drawable.ic_like_black);
                Toast.makeText(convertView.getContext(), "즐겨찾기에서 해제되었습니다.", Toast.LENGTH_SHORT).show();
            } else {
                //관심추가
                foodtruckFavoriteBtn.setImageResource(R.drawable.ic_like_red);
                Toast.makeText(convertView.getContext(), "즐겨찾기에 추가되었습니다.", Toast.LENGTH_SHORT).show();
            }

        }
    }
}