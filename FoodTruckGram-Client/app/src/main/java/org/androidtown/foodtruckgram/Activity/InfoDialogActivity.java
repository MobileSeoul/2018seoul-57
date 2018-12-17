package org.androidtown.foodtruckgram.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import org.androidtown.foodtruckgram.R;

public class InfoDialogActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_info_dialog);

        TextView howToUseTextView = (TextView)findViewById(R.id.howToUseTextView);
        howToUseTextView.append("\n1. 내 주변 영업중인 푸드트럭의 정보를 받아보세요.\n\n");
        howToUseTextView.append("2. 서울시에서 영업하는 모든 푸드트럭의 정보를 받아보세요.\n\n");
        howToUseTextView.append("3. 어플에서 미리 주문하고 가시면 기다리는 시간을 절약할 수 있어요.\n\n");
        howToUseTextView.append("4. 관심 푸드트럭을 저장하고 손쉽게 주문하고 영업시간을 알아보세요.\n\n");

        TextView finishBtn = (TextView)findViewById(R.id.finishBtn);
        finishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}
