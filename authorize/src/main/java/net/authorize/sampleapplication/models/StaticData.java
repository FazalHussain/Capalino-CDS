package net.authorize.sampleapplication.models;

import android.app.Notification;
import android.support.v7.app.NotificationCompat;

/**
 * Created by fazal on 11/3/2016.
 */

public class StaticData {


    public static String isannual;

    public static String isquarter;

    public static String paymentStatus;

    public static String Userid;

    public static String email;

    public static String expiry;

    public static String fname;

    public static String lname;

    public static String city;

    public static String state;

    public static String Address;

    public static String discount;

    public static String PromoType;

    public static String PromoCodeID;

    public static String TotalAmount;

    public static Notification buildForegroundNotification(NotificationCompat.Builder b) {
        b.setPriority(Notification.PRIORITY_MIN);
        b.setOngoing(true)
                .setContentTitle("Loading")
                .setSmallIcon(android.R.drawable.stat_sys_download)
                .setTicker("Loading");

        return(b.build());
    }

}
