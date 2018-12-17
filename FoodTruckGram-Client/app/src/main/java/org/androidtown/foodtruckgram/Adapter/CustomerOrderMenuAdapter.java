package org.androidtown.foodtruckgram.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.androidtown.foodtruckgram.Activity.CustomerHomeActivity;
import org.androidtown.foodtruckgram.Activity.CustomerOrderIdentifyDialog;
import org.androidtown.foodtruckgram.Activity.SellerHomeActivity;
import org.androidtown.foodtruckgram.Fragment.MyOrderFragment;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 이예지 on 2018-09-19.
 */

public class CustomerOrderMenuAdapter extends RecyclerView.Adapter<CustomerOrderMenuAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView menuName, menuPrice,orderBtn;
        public ImageView menuImage;
        private CustomerOrderMenuAdapter adapter;
        public Context context;

        public ViewHolder(final Context context, View itemView, final CustomerOrderMenuAdapter adapter) {
            super(itemView);
            this.context = context;
            this.adapter = adapter;

            menuName = (TextView)itemView.findViewById(R.id.customerOrderMenuName);
            menuPrice = (TextView)itemView.findViewById(R.id.customerOrderMenuPrice);
            orderBtn = (TextView)itemView.findViewById(R.id.customerOrderMenuBtn);
            menuImage = (ImageView)itemView.findViewById(R.id.customerOrderMenuImg);

        }
        @Override
        public void onClick(View v) {

        }
    }

    FoodTruckInfo foodTruckInfo;
    ArrayList<MenuInfo> menuInfos;
    public Context mContext;

    public CustomerOrderMenuAdapter(FoodTruckInfo foodTruckInfo, Context context){
        this.foodTruckInfo = foodTruckInfo;
        menuInfos = foodTruckInfo.getMenuList();
        mContext = context;
    }

    @Override
    public CustomerOrderMenuAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_customer_order_menu, parent, false);

        // Return a new holder instance
        CustomerOrderMenuAdapter.ViewHolder viewHolder = new CustomerOrderMenuAdapter.ViewHolder(context, contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomerOrderMenuAdapter.ViewHolder holder, final int position) {
        holder.menuName.setText(menuInfos.get(position).getMenuName());
        holder.menuPrice.setText(menuInfos.get(position).getMenuPrice());
        //holder.menuImage.setImageResource(menuInfos.get(position).getMenuImage());
        String base64 = null;
        if(menuInfos.size() > 0) {
            base64 = menuInfos.get(position).getMenuImage();
        }

        if(base64 != null && base64 != "") {
//            byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            holder.menuImage.setImageBitmap(bitmap);
            Picasso.with(mContext).load(base64).resize(300,320).into(holder.menuImage);
        }
        else {
            holder.menuImage.setImageResource(R.drawable.burger);
        }

        holder.orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (foodTruckInfo.isOpen().equals("true")) {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("userId", UserInfo.getUserInfo().getUserId());
                    params.put("tel", UserInfo.getUserInfo().getTel());
                    Date date = new Date(System.currentTimeMillis());
                    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
                    String new_date = date_format.format(date);
                    params.put("date", new_date);
                    params.put("storeName", foodTruckInfo.getStoreName());
                    Log.i("Order", foodTruckInfo.getStoreName());
                    params.put("menuName", menuInfos.get(position).getMenuName());
                    params.put("price", menuInfos.get(position).getMenuPrice());
                    params.put("menuImg", menuInfos.get(position).getMenuImage());
                    params.put("introduce",menuInfos.get(position).getMenuIntroduce());

                    Intent intent = new Intent(holder.context, CustomerOrderIdentifyDialog.class);
                    intent.putExtra("params", (HashMap) params);
                    ((Activity) holder.context).startActivityForResult(intent, 200);
                } else {
                    Toast.makeText(holder.context, "현재 영업중이지 않은 푸드트럭입니다. \n다음에 다시 이용해주세요", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        if(menuInfos!=null)
            return menuInfos.size();
        else {
            Log.i("Order",foodTruckInfo.getStoreName()+"메뉴 없음");
            return 0;
        }
    }

}
