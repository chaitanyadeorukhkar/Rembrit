package com.chaitanyad.rembrit;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class Action_Activity extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.cancel(Integer.parseInt(intent.getStringExtra("notifId")));
        DatabaseHelper db;
        db = new DatabaseHelper(context);
        db.deleteRow(intent.getStringExtra("notifId"));
        if (MainActivity.getInstance() != null)
            MainActivity.getInstance().populateListView();

    }
}