package com.MWBE.Connects.NY.GCM;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.MWBE.Connects.NY.Activities.BrowseActivity;
import com.MWBE.Connects.NY.Activities.HomeActivity;
import com.MWBE.Connects.NY.Activities.LoginActivity;
import com.MWBE.Connects.NY.Activities.TrackList;
import com.MWBE.Connects.NY.AppConstants.Utils;
import com.MWBE.Connects.NY.CapalinoServices.DailyMethodService;
import com.MWBE.Connects.NY.R;
import com.google.android.gms.gcm.GcmListenerService;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by fazal on 4/11/2017.
 */

public class GCMPushRecieverService extends GcmListenerService {

    private NotificationManager mNotificationManager;
    private int NOTIFICATION_ID = 0;
    private int index = 0;
    public static String rfp_match = "";
    private Utils utils;

    @Override
    public void onMessageReceived(String from, Bundle data) {
        utils = new Utils(this);

        sendNotification1(data.getString("message"),data.getString("direction"),data.getString("title"));

        Log.d("msg",data.getString("message"));
        Log.d("direction",data.getString("direction"));

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotification1(String message, String direction, String title) {

        mNotificationManager = (NotificationManager)
                this.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent();

        //Notification Authorization
        if(!utils.getdata("email").equalsIgnoreCase("") && !utils.getdata("pass").equalsIgnoreCase("")) {
            if (direction.equalsIgnoreCase("Announcement")) {

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("notif_status", "Announcement");
                intent.putExtra("notif_title", title);

            } else if (direction.equalsIgnoreCase("Alert")) {

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("notif_status", "Alert");
                intent.putExtra("notif_title", title);

            } else if (direction.equalsIgnoreCase("Event")) {

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("notif_status", "Event");
                intent.putExtra("notif_title", title);
            } else if (direction.equalsIgnoreCase("Browse")) {

                intent = new Intent(this, HomeActivity.class);
                //intent.putExtra("notif_status", "Browse");
                intent.putExtra("notif_status", "RFP Matched");
            } else if (direction.equalsIgnoreCase("RFP Matched")) {

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("notif_status", "RFP Matched");
            } else if (direction.equalsIgnoreCase("RFP Updated")) {

                intent = new Intent(this, HomeActivity.class);
                intent.putExtra("notif_status", "RFP Updated");
                intent.putExtra("notif_title", title);
            }
        }else {
            intent = new Intent(this, LoginActivity.class);
        }



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        String[] msg = message.split(":",2);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE), intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        String notifmsg = "";
        String titlemsg = "";

        for(int i=0;i<msg.length;i++){
            Log.d("msg",msg[i]);
        }

        try {
            if (msg.length > 1) {
                titlemsg = msg[0];
                notifmsg = msg[1].trim();
                Log.d("notif",msg[1].trim());
            } else {
                notifmsg = message;
                titlemsg = message;
            }

        }catch (ArrayIndexOutOfBoundsException e){
            e.printStackTrace();
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setSmallIcon(getNotificationIcon())
                .setColor(Color.GREEN)
                .setContentTitle(titlemsg)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

       /* remoteViews.setTextViewText(R.id.title,message);
        remoteViews.setImageViewResource(R.id.image,R.mipmap.icon);*/

        //Notification Draging code
        NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
        bigTextStyle.bigText(notifmsg);

        notificationBuilder.setStyle(bigTextStyle);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


        notificationManager.notify( (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE) /* ID of notification */, notificationBuilder.build());
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.mipmap.notificon : R.mipmap.icon;
    }
}
