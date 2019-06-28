package com.automobile.service.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.automobile.service.Activity.MenuBarActivity;
import com.automobile.service.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Belal on 12/8/2017.
 */

//class extending FirebaseMessagingService
public class MyFirebaseMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d("MyFirebaseMessaging", "onMessageReceived");

        Intent resultIntent = new Intent(getApplicationContext(), MenuBarActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(resultIntent);


        //if the message contains data payload
        //It is a map of custom keyvalues
        //we can read it easily
//        if (remoteMessage.getData().size() > 0) {
//            //getting the title and the body
//
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
//            displayNotification(title, body);
//
//        } else {
//            //getting the title and the body
//
//
//            String title = remoteMessage.getNotification().getTitle();
//            String body = remoteMessage.getNotification().getBody();
//            displayNotification(title, body);
//        }


    }


    @Override
    public void onRebind(Intent intent) {
        super.onRebind(intent);
        Log.d("MyFirebaseMessaging", "onRebind");
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("MyFirebaseMessaging", "onStart");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("MyFirebaseMessaging", "onCreate");


    }

    @Override
    public void handleIntent(Intent intent) {
        super.handleIntent(intent);

        Log.d("MyFirebaseMessaging", "handleIntent" + intent.getStringExtra(""));


        if (intent != null) {
            // String data = intent.getDataString("test");
            Log.d("MyFirebaseMessaging", "NOT NULL 1 " + intent.getDataString());
            Log.d("MyFirebaseMessaging", "NOT NULL 2 " + intent.getStringExtra("test"));
            Log.d("MyFirebaseMessaging", "NOT NULL 3 " + intent.getExtras().get("test"));
            Log.d("MyFirebaseMessaging", "NOT NULL 5 " + intent.getExtras().getString("gcm.notification.body"));
            Log.d("MyFirebaseMessaging", "NOT NULL 4 " + intent.getBundleExtra("test"));
        } else {
            Log.d("MyFirebaseMessaging", "NULL");
        }


    }

    @Override
    public void onMessageSent(String s) {
        super.onMessageSent(s);
        Log.d("MyFirebaseMessaging", "onMessageSent");
    }

    public void displayNotification(String title, String body) {

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(getApplicationContext(), "1235656564")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle(title)
                        .setAutoCancel(true)
                        .setContentText(body);


        /*
        *  Clicking on the notification will take us to this intent
        *  Right now we are using the MainActivity as this is the only activity we have in our application
        *  But for your project you can customize it as you want
        * */

        Intent resultIntent = new Intent(getApplicationContext(), MenuBarActivity.class);
        resultIntent.putExtra("test", "test");


        /*
        *  Now we will create a pending intent
        *  The method getActivity is taking 4 parameters
        *  All paramters are describing themselves
        *  0 is the request code (the second parameter)
        *  We can detect this code in the activity that will open by this we can get
        *  Which notification opened the activity
        * */
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /*
        *  Setting the pending intent to notification builder
        * */

        mBuilder.setContentIntent(pendingIntent);
        NotificationManager mNotifyMgr = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);



        /*
        * The first parameter is the notification id
        * better don't give a literal here (right now we are giving a int literal)
        * because using this id we can modify it later
        * */
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }


}