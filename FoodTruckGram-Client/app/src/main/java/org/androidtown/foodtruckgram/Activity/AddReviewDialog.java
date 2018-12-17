package org.androidtown.foodtruckgram.Activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.ReviewInfo;
import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Haneul on 2018-09-19.
 */

public class AddReviewDialog extends Dialog {

    EditText addReviewContent;
    Button saveBtn, cancelBtn;

    FoodTruckInfo foodTruckInfo;
    ReviewInfo reviewInfo;

    private String serverURL_InsertReview = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/c/insertReview";
    private AddReviewDialog.FoodTruckReviewDB foodTruckReviewDB;
    ArrayList<ReviewInfo> reviewInfos;

    public AddReviewDialog(Context context, final FoodTruckInfo foodTruckInfo, final ArrayList<ReviewInfo>reviewInfos) {

        super(context);
        this.foodTruckInfo = foodTruckInfo;
        this.reviewInfos = reviewInfos;
        requestWindowFeature(Window.FEATURE_NO_TITLE);   //다이얼로그의 타이틀바를 없애주는 옵션입니다.
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));  //다이얼로그의 배경을 투명으로 만듭니다.
        setContentView(R.layout.add_review_dialog);     //다이얼로그에서 사용할 레이아웃입니다.

        addReviewContent = (EditText) findViewById(R.id.add_review_content);
        saveBtn = (Button) findViewById(R.id.save_btn);
        cancelBtn = (Button) findViewById(R.id.cacel_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Toast.makeText(getContext(), ed1.getText().toString() + ed2.getText().toString(), Toast.LENGTH_LONG).show();
                dismiss();   //다이얼로그를 닫는 메소드입니다.
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String setDate = sdf.format(date);

                UserInfo userInfo = UserInfo.getUserInfo();

                reviewInfo = new ReviewInfo();
                reviewInfo.setDate(date);
                reviewInfo.setUserName(userInfo.getUserId());
                reviewInfo.setReview(addReviewContent.getText().toString());


                reviewInfos.add(reviewInfo);

                Map<String,String> params = new HashMap<String,String>();
                params.put("storeName",foodTruckInfo.getStoreName());
                params.put("userName",reviewInfo.getUserName());
                params.put("date", setDate);
                params.put("review",reviewInfo.getReview());

                addReviewContent.setText("");

                AddReviewDialog.FoodTruckReviewDB RDB = new AddReviewDialog.FoodTruckReviewDB();
                RDB.execute(params);
                // Toast.makeText(getContext(), ed1.getText().toString() + ed2.getText().toString(), Toast.LENGTH_LONG).show();

                dismiss();   //다이얼로그를 닫는 메소드입니다.

                Toast.makeText(getContext().getApplicationContext(), "리뷰가 작성되었습니다.", Toast.LENGTH_LONG).show();
            }
        });



        //리뷰 저장하는 리스너 만들어야 함!!

    }
    class FoodTruckReviewDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>... maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_InsertReview);
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
            Log.i("yunjae123", aVoid);

           /* if (aVoid.contains("")) {
                Toast.makeText(getContext().getApplicationContext(), "리뷰가 작성되었습니다.", Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                //startActivity(intent);
                getOwnerActivity().setResult(104);
            } else {
                //NOK
                Log.i("yunjae", "리뷰작성 오류");
                Toast.makeText(getContext().getApplicationContext(), "리뷰 작성 오류", Toast.LENGTH_SHORT).show();
            }*/
        }
    }
}
