package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.foodtruckgram.Activity.DetailActivity;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;

import java.util.ArrayList;

/**
 * Created by 이예지 on 2018-09-18.
 */

public class FavoriteTruckListAdapter extends RecyclerView.Adapter<FavoriteTruckListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView storeName, isOpen,favoriteDetailBtn;
        private FavoriteTruckListAdapter adapter;
        private Context context;

        public ViewHolder(final Context context, View itemView, final FavoriteTruckListAdapter adapter) {
            super(itemView);
            this.context = context;
            this.adapter = adapter;

            storeName = (TextView)itemView.findViewById(R.id.favoriteTruckName);
            isOpen = (TextView)itemView.findViewById(R.id.favoriteTruckIsOpen);
            favoriteDetailBtn = (TextView)itemView.findViewById(R.id.favoriteDetailBtn);
        }
        @Override
        public void onClick(View v) {

        }
    }

    private ArrayList<FoodTruckInfo> favoriteTruckList;

    public FavoriteTruckListAdapter(){
        favoriteTruckList = UserInfo.getUserInfo().getMyFavoriteList();
    }

    @Override
    public FavoriteTruckListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_favorite_foodtruck_list, parent, false);

        // Return a new holder instance
        FavoriteTruckListAdapter.ViewHolder viewHolder = new FavoriteTruckListAdapter.ViewHolder(context, contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final FavoriteTruckListAdapter.ViewHolder holder, final int position) {

        holder.storeName.setText(favoriteTruckList.get(position).getStoreName());
        Log.i("Favorite","Open = "+favoriteTruckList.get(position).isOpen());
        if(favoriteTruckList.get(position).isOpen().equals("true")) {
            holder.isOpen.setText("영업중");
        }
        else {
            holder.isOpen.setText("영업종료");
        }

        holder.favoriteDetailBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(holder.context, DetailActivity.class);
                intent.putExtra("foodtruckInfo", favoriteTruckList.get(position));
                holder.context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return favoriteTruckList.size();
    }
}
