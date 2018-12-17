package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.Info.OrderInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by user on 2018-09-19.
 */

public class CustomerMyOrderAdapter extends RecyclerView.Adapter<CustomerMyOrderAdapter.ViewHolder> {
    String serverURL_orderDelete = "http://"+ HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/deleteOrder";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView pot;
        public TextView storeName, date, menuName, price;
        private CustomerMyOrderAdapter adapter;
        private Context context;

        public ViewHolder(final Context context, View itemView, final CustomerMyOrderAdapter adapter) {
            super(itemView);

            this.context = context;
            this.adapter = adapter;

            pot = (ImageView)itemView.findViewById(R.id.pot);
            storeName = (TextView)itemView.findViewById(R.id.storename);
            date = (TextView)itemView.findViewById(R.id.date);
            menuName = (TextView)itemView.findViewById(R.id.menuName);
            price = (TextView)itemView.findViewById(R.id.price);
        }

        @Override
        public void onClick(View v) {

        }
    }

    // Store a member variable for the contacts
    private ArrayList<OrderInfo> myorderList;
    // Store the context for easy access
    private Context mContext;
    private TextView myorder_count;

    // Pass in the contact array into the constructor
    public CustomerMyOrderAdapter(Context context, ArrayList<OrderInfo> myorderList, TextView myorder_count) {
        this.myorderList = myorderList;
        mContext = context;
        this.myorder_count = myorder_count;
    }

    public void notifyData(ArrayList<OrderInfo> orderInfos){
        myorderList = orderInfos;
        Log.i("Order","notifyData()호출");
        notifyDataSetChanged();
    }

    public ArrayList<OrderInfo> getMyorderList() {
        return myorderList;
    }

    public void setMyorderList(ArrayList<OrderInfo> myorderList) {
        this.myorderList = myorderList;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_customer_myorder, parent, false);

        // Return a new holder instance
        CustomerMyOrderAdapter.ViewHolder viewHolder = new CustomerMyOrderAdapter.ViewHolder(context, contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        OrderInfo orderInfo = myorderList.get(position);

        // Set item views based on your views and data model
        //holder.deleteCheck.setImageResource(R.drawable.girl);  //menu image edit
        holder.storeName.setText(orderInfo.getStoreName());
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String new_date = date_format.format(orderInfo.getDate());
        holder.date.setText(new_date);
        holder.menuName.setText(orderInfo.getMenuName());
        holder.price.setText(Integer.toString(orderInfo.getPrice()) + " 원");
    }

    @Override
    public int getItemCount() {
        return myorderList.size();
    }


}
