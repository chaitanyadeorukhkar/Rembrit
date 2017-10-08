package com.chaitanyad.rembrit;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

/**
 * Created by deoru on 8/29/2016.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        DatabaseHelper db;
        db = new DatabaseHelper(context);

        Cursor res = db.getMaxId();
        StringBuffer buffer = new StringBuffer();
        if (res.getCount() == 0) {
            // Toast.makeText(getContext(), "Empty DB", Toast.LENGTH_SHORT).show();
        } else {
            while (res.moveToNext()) {
                buffer.append(res.getString(0));

            }
            // Toast.makeText(getContext(), buffer.toString(), Toast.LENGTH_LONG).show();
        }

        //Intent intent = new Intent(getApplicationContext(), MainActivity.class); //open app on click
        Intent actionIntent = new Intent(context, Action_Activity.class);
        actionIntent.setAction("com.chaitanyad.rembrit.Action_Activity.notifier");
        actionIntent.putExtra("notifId", buffer.toString());

       /* TaskStackBuilder taskStackBuilder= TaskStackBuilder.create(context.getApplicationContext()); //open app on click
        taskStackBuilder.addParentStack(MainActivity.class); //open app on click
        taskStackBuilder.addNextIntent(intent); //open app on click
        PendingIntent pendingIntent=taskStackBuilder.getPendingIntent(123,PendingIntent.FLAG_UPDATE_CURRENT); //open app on click*/

        if(buffer.toString().equals("null"))
        {}
        else {
            //PendingIntent actionPendingIntent = PendingIntent.getActivity(context, Integer.parseInt(buffer.toString()), actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            PendingIntent actionPendingIntent = PendingIntent.getBroadcast(context, Integer.parseInt(buffer.toString()), actionIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            // actionIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            String text = intent.getStringExtra("a");

            NotificationCompat.Builder nBuilder = new NotificationCompat.Builder(context);

            nBuilder.setContentTitle(text);
            //nBuilder.setContentText("Take book bank");

            nBuilder.setSmallIcon(android.R.drawable.ic_popup_reminder);
            //nBuilder.setContentIntent(pendingIntent); //open app on click
            nBuilder.setDefaults(Notification.DEFAULT_SOUND);
            nBuilder.setVibrate(new long[]{100, 1000});
            nBuilder.setLights(Color.argb(255,48,63,159), 400, 400);
            //nBuilder.setLights(context.getResources().getColor( R.color.colorPrimaryDark), 400, 400);
            nBuilder.setAutoCancel(false);
            nBuilder.setOngoing(true);
            nBuilder.addAction(R.drawable.ic_done_black_24dp, "Done", actionPendingIntent);
            Notification notification = nBuilder.build();
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            //  Toast.makeText(context,"Triggered",Toast.LENGTH_LONG).show();


            // notificationManager.notify(Integer.parseInt(buffer.toString()), notification);
            notificationManager.notify(Integer.parseInt(buffer.toString()), notification);
        }
    }
}