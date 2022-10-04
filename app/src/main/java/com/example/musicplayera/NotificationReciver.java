package com.example.musicplayera;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationReciver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if(intent.getAction()!=null){
            MainActivity.getInstance(context).fromNotification(intent.getAction());
        }
        else {
            MainActivity.getInstance(context).km();
        }
//        MainActivity.getInstance(context).fromNotification(intent.getExtras().getString("key"));
    }


    }
