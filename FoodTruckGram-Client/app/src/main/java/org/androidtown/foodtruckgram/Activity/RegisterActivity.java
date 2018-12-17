package org.androidtown.foodtruckgram.Activity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    String userId, password, userName, role, tel;
    boolean isCheckId = false;
    String serverURL_duplicate_check = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/Android_login_duplicate_check";
    String serverURL_register = "http://" + HttpClient.ipAdress + HttpClient.serverPort + HttpClient.urlBase + "/Android_register";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            getSupportActionBar().setCustomView(R.layout.custom_action_bar);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        setContentView(R.layout.activity_register);

        TextView checkIdBtn = (TextView) findViewById(R.id.checkIdBtn);
        checkIdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //서버에 아이디 중복 확인
                checkID();

            }
        });

        TextView submitBtn = (TextView) findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheckId == false) {
                    Toast.makeText(getApplicationContext(), "아이디 중복을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    if (checkRole()) {
                        if (checkPW() && checkTel()) {

                            userName = ((EditText) findViewById(R.id.editText_name)).getText().toString();

                            Map<String, String> params = new HashMap<String, String>();
                            params.put("userId", userId);
                            params.put("password", password);
                            params.put("userName", userName);
                            params.put("role", role);
                            params.put("tel", tel);


                            registDB RDB = new registDB();
                            RDB.execute(params);
                        }
                    }
                    else {
                        finish();
                    }
                }

            }
        });

    }

    private boolean checkTel(){
        tel = ((EditText) findViewById(R.id.editText_tel)).getText().toString();
        if(tel.length()==11) {
            return true;
        }
        else
        {
            Toast.makeText(getApplicationContext(),"전화번호가 잘못되었습니다.",Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private boolean checkPW(){

        password = ((EditText)findViewById(R.id.editText_pw)).getText().toString();

        if(password != null && password != "") {
            return true;
        }
        else
            return false;

    }

    private boolean checkRole(){
        if(((RadioButton)findViewById(R.id.radioButton_seller)).isChecked()){
            //role="S";
            //role="C";
            Toast.makeText(getApplicationContext(),"판매자는 운영측에 사전 등록 후 이용해주세요.\nSeller Version Test용 id:aaa,pw:1234",Toast.LENGTH_LONG).show();
            return false;
        }
        else
            role="C";
        return true;
    }

    private void checkID() {
        //서버에 아이디 보내서 중복 확인
        userId = ((EditText) findViewById(R.id.editText_id)).getText().toString();

        Map<String, String> params = new HashMap<String, String>();
        params.put("userId", userId);

        idCheckDB IDB = new idCheckDB();
        IDB.execute(params);

    }

    class idCheckDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST", serverURL_duplicate_check);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드"+statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : "+body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            if(aVoid.contains("NOK")){
                Log.i("yunjae", "아이디 중복");
                ((EditText)findViewById(R.id.editText_id)).setText("");
                Toast.makeText(getApplicationContext(),"이미 존재하는 아이디 입니다.",Toast.LENGTH_SHORT).show();
                isCheckId=false;
            }
            else{
                //OK
                Toast.makeText(getApplicationContext(),"사용가능한 아이디 입니다.",Toast.LENGTH_SHORT).show();
                ((EditText)findViewById(R.id.editText_id)).setClickable(false);
                isCheckId=true;
            }

        }
    }

    class registDB extends AsyncTask<Map<String, String>, Integer, String> {

        @Override
        protected String doInBackground(Map<String, String>...maps) {

            HttpClient.Builder http = new HttpClient.Builder("POST",serverURL_register);
            http.addAllParameters(maps[0]);

            HttpClient post = http.create();
            post.request();

            int statusCode = post.getHttpStatusCode();

            Log.i("yunjae", "응답코드"+statusCode);

            String body = post.getBody();

            Log.i("yunjae", "body : "+body);

            return body;

        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            Log.i("yunjae", aVoid);

            if(aVoid.contains("OK")){
                Toast.makeText(getApplicationContext(),"회원가입을 환영합니다. 로그인 후 이용해 주세요.",Toast.LENGTH_LONG).show();
                //Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                //startActivity(intent);
                setResult(104);

                finish();
            }
            else{
                //NOK
                Log.i("yunjae", "회원가입 오류");
                Toast.makeText(getApplicationContext(),"회원가입 오류",Toast.LENGTH_SHORT).show();
            }

        }
    }
}
