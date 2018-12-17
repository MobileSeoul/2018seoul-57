package org.androidtown.foodtruckgram.Activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import org.androidtown.foodtruckgram.Info.UserInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private String userId, password;
    String serverURL = "http://"+HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/Android_login";
    UserInfo userInfo;
    private final String TAG = "LoginActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setContentView(R.layout.activity_login);


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(104);
    }

    public void onButtonLogin(View v){
        userId = ((EditText) findViewById(R.id.etId)).getText().toString();
        password = ((EditText) findViewById(R.id.etPassword)).getText().toString();

        //로그인 확인 작업
        //Userinfo의 role확인
        //Seller페이지 Customer페이지 구분해서 넘기기


        Map<String,String> params = new HashMap<String,String>();
        params.put("userId",userId);
        params.put("password",password);

        loginDB IDB = new loginDB();
        IDB.execute(params);

    }

    public void onButtonRegister(View v) {
        Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(intent);
    }


    class loginDB extends AsyncTask<Map<String, String>, Integer, String> {

        String data=null;
        String receiveMsg="";
        //id, pw, 나이, 성별, 보유쿠폰, 즐겨찾기정보
        String userName, role, tel;


        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST",serverURL);
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
            Log.i(TAG, aVoid);

            if(aVoid.contains("fail")){
                Log.i(TAG, "로그인 실패");
                Toast.makeText(getApplicationContext(),"로그인 실패",Toast.LENGTH_SHORT).show();
            }
            else{
                Gson gson = new Gson();
                userInfo = UserInfo.getUserInfo();

                UserInfo info = gson.fromJson(aVoid,UserInfo.class);

                userInfo.setUserId(info.getUserId());
                userInfo.setUserName(info.getUserName());
                userInfo.setRole(info.getRole());
                userInfo.setTel(info.getTel());


                Log.i(TAG, userInfo.getUserName()+"/"+userInfo.getRole());


                if(info.getRole().equals("S")){

                    Intent intent = new Intent(getApplicationContext(),SellerHomeActivity.class);
                    intent.putExtra("userInfo",userInfo);
                    setResult(104, intent);
                    startActivityForResult(intent, 104);
                    finish();
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),CustomerHomeActivity.class);
                    startActivity(intent);
                    finish();
                }

            }

        }

    }

}
