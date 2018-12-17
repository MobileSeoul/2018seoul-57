package org.androidtown.foodtruckgram.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSReceiver extends BroadcastReceiver {


    private static SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        Bundle bundle = intent.getExtras();

        Object messages[] = (Object[]) bundle.get("pdus");

        if(messages!=null){

            SmsMessage smsMessage[] = new SmsMessage[messages.length];


            for (int i = 0; i < messages.length; i++) {

                // PDU 포맷으로 되어 있는 메시지를 복원합니다.

                smsMessage[i] = SmsMessage.createFromPdu((byte[]) messages[i]);

            }

            // SMS 수신 시간 확인

            Date curDate = new Date(smsMessage[0].getTimestampMillis());

            Log.d("문자 수신 시간", curDate.toString());


            // SMS 발신 번호 확인

            String origNumber = smsMessage[0].getOriginatingAddress();


            // SMS 메시지 확인

            String message = smsMessage[0].getMessageBody().toString();

            Log.d("문자 내용", "발신자 : " + origNumber + ", 내용 : " + message);
        }

    }


    private SmsMessage[] parseSmsMessage(Bundle bundle) {
        Object[] objs = (Object[]) bundle.get("pdus");
        SmsMessage[] messages = new SmsMessage[objs.length];

        for (int i = 0; i < objs.length; i++) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                String format = bundle.getString("format");
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i], format);
            } else {
                messages[i] = SmsMessage.createFromPdu((byte[]) objs[i]);
            }
        }

        return messages;
    }
}
