package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidtown.foodtruckgram.Activity.SellerMenuEditActivity;
import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class SellerMenuListAdapter extends RecyclerView.Adapter<SellerMenuListAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public ImageView menuImage;
        public TextView name, price, introduce, menuImageURI;
        public ImageButton remove, edit;
        private SellerMenuListAdapter adapter;
        private Context context;
        private LinearLayout menulayout;

        String serverURL = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/updateMenu";

        public ViewHolder(final Context context, View itemView, final SellerMenuListAdapter adapter, final FoodTruckInfo foodTruckInfo) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            this.context = context;
            this.adapter = adapter;

            menuImage = (ImageView) itemView.findViewById(R.id.menuImage_imageView);
            name = (TextView) itemView.findViewById(R.id.menuName_textView);
            price = (TextView) itemView.findViewById(R.id.menuPrice_textView);
            introduce = (TextView) itemView.findViewById(R.id.menuIntroduce_textView);
            menuImageURI = (TextView) itemView.findViewById(R.id.menuImageURI);

            remove = (ImageButton) itemView.findViewById(R.id.menuItemRemoveBtn);
            remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getLayoutPosition();
                    Toast.makeText(context, name.getText() + Integer.toString(position) + "가 삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    adapter.removeItem(position);

                }
            });

          //  edit = (ImageButton)itemView.findViewById(R.id.menuItemEditBtn);

            menulayout = (LinearLayout)itemView.findViewById(R.id.menu_layout);

            menulayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getLayoutPosition();
                    //Toast.makeText(context, name.getText() + Integer.toString(position) + "Edit", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(context, SellerMenuEditActivity.class);
                    intent.putExtra("foodTruckInfo", foodTruckInfo);
                    intent.putExtra("menuImage", menuImageURI.getText());
                    intent.putExtra("menuName", name.getText());
                    intent.putExtra("menuPrice", price.getText());
                    intent.putExtra("menuIntroduce", introduce.getText());
                    intent.putExtra("serverURL", serverURL);
                    context.startActivity(intent);
                }
            });

        }

        @Override
        public void onClick(View view) {
            int position = getLayoutPosition(); // gets item position
            // We can access the data within the views
            //Toast.makeText(context, name.getText() + Integer.toString(position), Toast.LENGTH_SHORT).show();
            adapter.removeItem(position);

        }
    }

    // Store a member variable for the contacts
    private ArrayList<MenuInfo> menuList;
    // Store the context for easy access
    private Context mContext;
    FoodTruckInfo foodTruckInfo;
    String serverURL = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/s/deleteMenu";

    // Pass in the contact array into the constructor
    public SellerMenuListAdapter(Context context, ArrayList<MenuInfo> menuList, FoodTruckInfo foodTruckInfo) {
        this.menuList = menuList;
        mContext = context;
        this.foodTruckInfo = foodTruckInfo;
    }

    // Easy access to the context object in the recyclerview
    private Context getContext() {
        return mContext;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public SellerMenuListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View contactView = inflater.inflate(R.layout.item_seller_menu, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(context, contactView, this, foodTruckInfo);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(SellerMenuListAdapter.ViewHolder viewHolder, int position) {
        // Get the data model based on position
        MenuInfo menuInfo = menuList.get(position);

        // Set item views based on your views and data model
        //viewHolder.menuImage.setImageResource(R.drawable.burger);  //menu image edit
        String base64 = menuInfo.getMenuImage();
        Log.i("Edit", "base64 = " + base64);
        if (base64 != null && base64 != "") {
//            byte[] decodedString = Base64.decode(base64, Base64.NO_WRAP);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
//            viewHolder.menuImage.setImageBitmap(bitmap);
//            Log.i("Edit", "menuImage = " + base64);
            Picasso.with(getContext()).load(base64).into(viewHolder.menuImage);
        } else {
            viewHolder.menuImage.setImageResource(R.drawable.burger);
        }


        viewHolder.name.setText(menuInfo.getMenuName());
        viewHolder.price.setText(menuInfo.getMenuPrice());
        viewHolder.introduce.setText(menuInfo.getMenuIntroduce());
        Log.i("Edit", "introduce = " + menuInfo.getMenuIntroduce());
        viewHolder.menuImageURI.setText(menuInfo.getMenuImage());

        Log.i("RecyclerView", "name : " + menuInfo.getMenuName() + " / price : " + menuInfo.getMenuPrice());


    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return menuList.size();
    }

    public void removeItem(int p) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("storeName", foodTruckInfo.getStoreName());
        params.put("menuName", menuList.get(p).getMenuName());

        MenuDeleteDB menuDeleteDB = new MenuDeleteDB();
        menuDeleteDB.execute(params);

        menuList.remove(p);
        notifyItemRemoved(p);
    }

    public void notifyData(FoodTruckInfo foodTruckInfo) {
        menuList = foodTruckInfo.getMenuList();
        Log.i("Edit", "notifyData()호출");
        Log.i("Edit", "foodTruckInfo.getMenuList().size() : " + foodTruckInfo.getMenuList().size());
        notifyDataSetChanged();
    }

    class MenuDeleteDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {
            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL);
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
