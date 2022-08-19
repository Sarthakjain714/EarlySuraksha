package com.example.earlysuraksha;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Switch;

//import com.google.android.gms.auth.api.phone.SmsRetriever;
//import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

//public class SmsBroadcastReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        if(intent.getAction()== SmsRetriever.SMS_RETRIEVED_ACTION){
//            Bundle extras =intent.getExtras();
//
//            Status smsRetreiverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
//            switch (smsRetreiverStatus.getStatusCode()){
//                case CommonStatusCodes
//                        .SUCCESS:
////                    Intent messageIntent = extras.getParcelable(SmsRetriever.);
//            }
//        }
//    }
//
//
//    public interface sendBroadcastReceiverListener{
//        void onSuccess(Intent intent);
//
//        void onFailure();
//    }
//}
