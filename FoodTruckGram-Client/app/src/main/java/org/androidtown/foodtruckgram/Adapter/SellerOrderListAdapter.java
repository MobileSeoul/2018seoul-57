package org.androidtown.foodtruckgram.Adapter;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.foodtruckgram.Info.OrderInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 2018-09-18.
 */

public class SellerOrderListAdapter extends RecyclerView.Adapter<SellerOrderListAdapter.ViewHolder> {
    String serverURL_orderDelete = "http://"+ HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/deleteOrder";

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView deleteCheck, message;
        public TextView userId, tel, menuName, price;
        private SellerOrderListAdapter adapter;
        private Context context;


        public ViewHolder(final Context context, View itemView, final SellerOrderListAdapter adapter) {
            super(itemView);

            this.context = context;
            this.adapter = adapter;

            deleteCheck = (ImageView)itemView.findViewById(R.id.delete);
            message = (ImageView)itemView.findViewById(R.id.message);
            userId = (TextView)itemView.findViewById(R.id.userId);
            tel = (TextView)itemView.findViewById(R.id.tel);
            menuName = (TextView)itemView.findViewById(R.id.menuName);
            price = (TextView)itemView.findViewById(R.id.price);

            deleteCheck.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    adapter.removeItem(position);
                }
            });

            message.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    adapter.sendSMS(position);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }

    public void removeItem(int p) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("storeName",orderList.get(p).getStoreName());
        params.put("userId",orderList.get(p).getUserId());
        params.put("menuName",orderList.get(p).getMenuName());


        OrderDeleteDB orderDeleteDB = new OrderDeleteDB();
        orderDeleteDB.execute(params);


        orderList.remove(p);
        order_count.setText(Integer.toString(getItemCount()));
        notifyItemRemoved(p);
    }

    private void sendSMS(int p)
    {
        String phoneNumber = orderList.get(p).getTel();
        String message = orderList.get(p).getMenuName() + "메뉴가 주문이 완료되었습니다.";

        Log.i("yunjae", "phoneNumber = " + phoneNumber + " message = " + message);
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(SENT), 0);
        PendingIntent deliveredPI = PendingIntent.getBroadcast(getContext(), 0, new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        getContext().registerReceiver(new BroadcastReceiver() {
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        Toast.makeText(getContext(), "알림 문자 메시지가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        Toast.makeText(getContext(),"전송을 완료하였습니다",Toast.LENGTH_LONG).show();
    }



    // Store a member variable for the contacts
    private ArrayList<OrderInfo> orderList;
    // Store the context for easy access
    private Context mContext;
    private TextView order_count;

    // Pass in the contact array into the constructor
    public SellerOrderListAdapter(Context context, ArrayList<OrderInfo> orderList, TextView order_count) {
        this.orderList = orderList;
        mContext = context;
        this.order_count = order_count;
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
        View contactView = inflater.inflate(R.layout.item_seller_order, parent, false);

        // Return a new holder instance
        SellerOrderListAdapter.ViewHolder viewHolder = new SellerOrderListAdapter.ViewHolder(context, contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        // Get the data model based on position
        OrderInfo orderInfo = orderList.get(position);

        // Set item views based on your views and data model
        //holder.deleteCheck.setImageResource(R.drawable.girl);  //menu image edit
        holder.userId.setText(orderInfo.getUserId());
        holder.tel.setText(orderInfo.getTel());
        holder.menuName.setText(orderInfo.getMenuName());
        holder.price.setText(Integer.toString(orderInfo.getPrice()));
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    class OrderDeleteDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_orderDelete);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            //Log.i(TAG, "응답코드"+statusCode);

            String body = post.getBody();

            //Log.i(TAG, "body : "+body);

            return body;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);

        }

    }


}
