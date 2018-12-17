package org.androidtown.foodtruckgram.Adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.androidtown.foodtruckgram.R;

import java.util.ArrayList;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * Created by 이예지 on 2018-09-17.
 */

public class HomeAutoImgScrollAdapter extends PagerAdapter {
    Context context;
    ArrayList<String> data;

    public HomeAutoImgScrollAdapter(Context context, ArrayList<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        //뷰페이지 슬라이딩 할 레이아웃 인플레이션
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_home_auto_image,null);
        ImageView image_container = (ImageView) v.findViewById(R.id.homeImgContainer);
        /*if(position%2==0)
            image_container.setImageResource(R.drawable.sample_img); //이부분을 데이터셋의 이미지경로로 넣기!
        else
            image_container.setImageResource(R.drawable.festival);*/
        if(position==0)
            image_container.setImageResource(R.drawable.sample_img);
        else if(position==1)
            image_container.setImageResource(R.drawable.festival);
        else
            image_container.setImageResource(R.drawable.sample_img2);
        //Glide.with(context).load(data.get(position)).into(image_container);
        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View)object);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
