package org.androidtown.foodtruckgram.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.androidtown.foodtruckgram.Activity.CustomerCouponActivity;
import org.androidtown.foodtruckgram.Activity.FavoriteActivity;
import org.androidtown.foodtruckgram.Activity.InfoDialogActivity;
import org.androidtown.foodtruckgram.Adapter.HomeAutoImgScrollAdapter;
import org.androidtown.foodtruckgram.R;

import java.util.ArrayList;

import cn.trinea.android.view.autoscrollviewpager.AutoScrollViewPager;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    AutoScrollViewPager autoScrollViewPager;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ArrayList<String> data = new ArrayList<>(); //이미지 url를 저장하는 arraylist
        data.add("1");
        data.add("2");
        data.add("3");

        /*data.add("https://upload.wikimedia.org/wikipedia/en/thumb/2/24/SpongeBob_SquarePants_logo.svg/1200px-SpongeBob_SquarePants_logo.svg.png");
        data.add("http://nick.mtvnimages.com/nick/promos-thumbs/videos/spongebob-squarepants/rainbow-meme-video/spongebob-rainbow-meme-video-16x9.jpg?quality=0.60");
        data.add("http://nick.mtvnimages.com/nick/video/images/nick/sb-053-16x9.jpg?maxdimension=&quality=0.60");
        data.add("https://www.gannett-cdn.com/-mm-/60f7e37cc9fdd931c890c156949aafce3b65fd8c/c=243-0-1437-898&r=x408&c=540x405/local/-/media/2017/03/14/USATODAY/USATODAY/636250854246773757-XXX-IMG-WTW-SPONGEBOB01-0105-1-1-NC9J38E8.JPG");
*/
        autoScrollViewPager = (AutoScrollViewPager)view.findViewById(R.id.homeImgViewPager);
        HomeAutoImgScrollAdapter scrollAdapter = new HomeAutoImgScrollAdapter(getContext(), data);
        autoScrollViewPager.setAdapter(scrollAdapter); //Auto Viewpager에 Adapter 장착
        autoScrollViewPager.setInterval(3000); // 페이지 넘어갈 시간 간격 설정
        autoScrollViewPager.startAutoScroll(); //Auto Scroll 시작

        ImageView info = (ImageView)view.findViewById(R.id.homeInfoBtn);
        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), InfoDialogActivity.class);
                startActivity(intent);
            }
        });
        ImageView coupon = (ImageView)view.findViewById(R.id.homeCouponBtn);
        coupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), CustomerCouponActivity.class);
                startActivity(intent);
            }
        });
        ImageView favorite = (ImageView)view.findViewById(R.id.homeFavoriteBtn);
        favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), FavoriteActivity.class);
                startActivity(intent);
            }
        });



        return view;
    }

}
