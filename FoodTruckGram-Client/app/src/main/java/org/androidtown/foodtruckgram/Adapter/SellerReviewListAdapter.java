package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.androidtown.foodtruckgram.Info.ReviewInfo;
import org.androidtown.foodtruckgram.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by user on 2018-09-13.
 */

public class SellerReviewListAdapter extends RecyclerView.Adapter<SellerReviewListAdapter.ViewHolder> {



    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView image;
        public TextView userName, date, review;
        private SellerReviewListAdapter adapter;
        private Context context;

        public ViewHolder(final Context context, View itemView, final SellerReviewListAdapter adapter) {
            super(itemView);

            this.context = context;
            this.adapter = adapter;

            image = (ImageView)itemView.findViewById(R.id.image);
            userName = (TextView)itemView.findViewById(R.id.userName);
            date = (TextView)itemView.findViewById(R.id.date);
            review = (TextView)itemView.findViewById(R.id.review);

        }

        @Override
        public void onClick(View v) {

        }
    }

    // Store a member variable for the contacts
    private ArrayList<ReviewInfo> reviewList;
    // Store the context for easy access
    private Context mContext;
    private TextView review_count;

    // Pass in the contact array into the constructor
    public SellerReviewListAdapter(Context context, ArrayList<ReviewInfo> reviewList, TextView review_count) {
        this.reviewList = reviewList;
        mContext = context;
        this.review_count = review_count;
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
        View contactView = inflater.inflate(R.layout.item_seller_review, parent, false);

        // Return a new holder instance
        SellerReviewListAdapter.ViewHolder viewHolder = new SellerReviewListAdapter.ViewHolder(context, contactView, this);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Get the data model based on position
        ReviewInfo reviewInfo = reviewList.get(position);

        // Set item views based on your views and data model
        holder.image.setImageResource(R.drawable.girl);  //menu image edit
        holder.userName.setText(reviewInfo.getUserName());
        SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
        String new_date = date_format.format(reviewInfo.getDate());
        holder.date.setText(new_date);
        holder.review.setText(reviewInfo.getReview());

        Log.i("RecyclerView","name : "+ reviewInfo.getUserName() + " / getReview : "+reviewInfo.getReview() + "////// " + reviewInfo.getDate());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

}
