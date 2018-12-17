package org.androidtown.foodtruckgram.Activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.androidtown.foodtruckgram.Info.FoodTruckInfo;
import org.androidtown.foodtruckgram.Info.MenuInfo;
import org.androidtown.foodtruckgram.R;
import org.androidtown.foodtruckgram.Server.HttpClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;

public class SellerMenuEditActivity extends AppCompatActivity {

    FoodTruckInfo foodTruckInfo;
    String menuName, menuPrice, menuIntroduce, menuImage;
    private Bitmap menuBitmap;
    EditText menuEdit_name, menuEdit_price, menuEdit_introduce;
    ImageView imageView;
    String serverURL;
    TextView menuEdit_image;
    int REQ_CODE_PICK_PICTURE = 110;
    String image = null;
    boolean check = false;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_menu_edit);

        Intent intent = getIntent();
        foodTruckInfo = (FoodTruckInfo) intent.getSerializableExtra("foodTruckInfo");
        menuName = intent.getStringExtra("menuName");
        menuPrice = intent.getStringExtra("menuPrice");
        menuIntroduce = intent.getStringExtra("menuIntroduce");
        menuImage = intent.getStringExtra("menuImage");
        serverURL = intent.getStringExtra("serverURL");


        menuEdit_name = findViewById(R.id.menuEdit_name);
        if (menuName != null) {
            //메뉴 수정시 이름은 수정 불가
            menuEdit_name.setEnabled(false);
            menuEdit_name.setBackground(null);
        }
        menuEdit_name.setText(menuName);

        menuEdit_price = findViewById(R.id.menuEdit_price);
        menuEdit_price.setText(menuPrice);

        menuEdit_introduce = findViewById(R.id.menuEdit_introduce);
        menuEdit_introduce.setText(menuIntroduce);

        imageView = (ImageView)findViewById(R.id.imageView);
        if(menuImage != null && !menuImage.equals("")) {
//            byte[] decodedString = Base64.decode(menuImage, Base64.NO_WRAP);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Picasso.with(this).load(menuImage).into(imageView);
            //imageView.setImageBitmap(bitmap);
        }
        else {
            imageView.setImageResource(R.drawable.burger);
        }



        TextView menuImageEditBtn = (TextView) findViewById(R.id.menuEdit_image);
        menuImageEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Image Change

            }
        });

        TextView menuEditCancelBtn = (TextView) findViewById(R.id.editCancelBtn);

        menuEditCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        final TextView menuEditBtn = (TextView) findViewById(R.id.menuEditBtn);
        menuEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String menuName = menuEdit_name.getText().toString();
                String menuPrice = menuEdit_price.getText().toString();
                String menuIntroduce = menuEdit_introduce.getText().toString();

                if (menuName == null || menuPrice == null)
                    Toast.makeText(getApplicationContext(), "메뉴 이름과 가격은 필수 입력입니다.", Toast.LENGTH_SHORT).show();
                else {
                    //image upload

                    //Map<String, String> params2 = new HashMap<>();
                    //params2.put("fileName", image);

                    String file = image;
                    //FileInputStream fis = new FileInputStream(file);
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                    Bitmap bitmap = BitmapFactory.decodeFile(file, options);
//
//                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
//                    String base64 = null;
//                    if(baos != null && file != null) {
//                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
//                        byte[] byteData = baos.toByteArray();
//
//                        base64 = Base64.encodeToString(byteData, Base64.NO_WRAP);
//                        menuImage = base64;
//                    }

                    Map<String, String> params = new HashMap<String, String>();
                    params.put("menuName", menuName);
//                    if(base64 != null) {
//                        params.put("menuImage", base64);
//                    }
//                    else {
                    if(menuImage != null && !menuImage.equals("")) {
//            byte[] decodedString = Base64.decode(menuImage, Base64.NO_WRAP);
//            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        //Picasso.with(this).load(menuImage).into(imageView);
                        //imageView.setImageBitmap(bitmap);
                        params.put("menuImage", menuImage);
                    }
                    else {
                        params.put("menuImage", "");
                    }

                    params.put("menuPrice", menuPrice);
                    params.put("menuIntroduce", menuIntroduce);
                    params.put("storeName", foodTruckInfo.getStoreName());

                    MenuUpdateAndInsetDB menuUpdateAndInsetDB = new MenuUpdateAndInsetDB();
                    menuUpdateAndInsetDB.execute(params);

                    Toast.makeText(getApplicationContext(), "메뉴가 추가/수정 되었습니다.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        menuEdit_image = (TextView) findViewById(R.id.menuEdit_image);
        menuEdit_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkPermission();
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivityForResult(intent, REQ_CODE_PICK_PICTURE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_CODE_PICK_PICTURE) {
            if (resultCode == Activity.RESULT_OK) {
                ImageView img = (ImageView) findViewById(R.id.imageView);
                img.setImageURI(data.getData()); // 사진 선택한 사진URI로 연결하기
                image = getRealPathFromURI(data.getData());
                menuImage = getRealPathFromURI(data.getData());
                Log.i("ImageURI", "uri = " + data.getData().getEncodedPath());
                Toast.makeText(getApplicationContext(), "용량이 초과되어 사진을 첨부할 수 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public String getRealPathFromURI(Uri contentURI) {

        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();

        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }

        Log.i("yunjae", "img filename = " + result);
        return result;

    }


    class MenuUpdateAndInsetDB extends AsyncTask<Map<String, String>, Integer, String> {

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

            /*MenuInfo menuInfo = new MenuInfo(menuEdit_name.getText().toString(), menuEdit_price.getText().toString(), menuImage, menuEdit_introduce.getText().toString());
            //foodTruckInfo.editMenu(menuInfo);
            Toast.makeText(getApplicationContext(), "메뉴 수정/추가 완료", Toast.LENGTH_SHORT).show();

            Log.i("Edit", "메뉴 수정/추가 완료");
            Log.i("Edit", "foodTruckInfo.getMenuList().size() = " + foodTruckInfo.getMenuList().size() + " menuImage = " + menuEdit_introduce.getText().toString());
*/
            Intent resultIntent = new Intent();
            resultIntent.putExtra("foodTruckInfo", foodTruckInfo);
            setResult(RESULT_OK, resultIntent);

            finish();
        }
    }

    private void checkPermission() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { // 마시멜로우 버전과 같거나 이상이라면
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "외부 저장소 사용을 위해 읽기/쓰기 필요", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]
                                {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                        2);  //마지막 인자는 체크해야될 권한 갯수

            } else {
                //Toast.makeText(this, "권한 승인되었음", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResult) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        //위 예시에서 requestPermission 메서드를 썼을시 , 마지막 매개변수에 0을 넣어 줬으므로, 매칭
        if (requestCode == 0) {
            // requestPermission의 두번째 매개변수는 배열이므로 아이템이 여러개 있을 수 있기 때문에 결과를 배열로 받는다.
            // 해당 예시는 요청 퍼미션이 한개 이므로 i=0 만 호출한다.
            if (grantResult[0] == 0) {
                //해당 권한이 승낙된 경우.
            } else {
                //해당 권한이 거절된 경우.

            }
        }
    }

}
