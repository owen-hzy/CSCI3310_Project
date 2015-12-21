package csci3310.cuhk.edu.hk.project.Reminder;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import csci3310.cuhk.edu.hk.project.MainActivity;
import csci3310.cuhk.edu.hk.project.R;

/**
 * Created by csamyphew on 21/12/15.
 */
public class NotifyService extends Service {

    private NotificationManager myNotificationManager;
    /**
     * Class for clients to access
     */
    public class ServiceBinder extends Binder {

    }

//    // Unique id to identify the notification.
//    private static final int NOTIFICATION = 123;
//    // Name of an intent extra we can use to identify if this service was started to create a notification
//    public static final String INTENT_NOTIFY = "ccsci3310.cuhk.edu.hk.project.Reminder";
//    // The system notification manager
//    private NotificationManager mNM;

    @Override
    public void onCreate() {
        Log.i("NotifyService", "onCreate()");

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("(time)LocalService", "Received start id " + startId + ": " + intent);

        showNotification();

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // This is the object that receives interactions from clients
    private final IBinder mBinder = new ServiceBinder();

    /**
     * Creates a notification and shows it in the OS drag-down status bar
     */
    private void showNotification() {
        Log.i("Start", "notification");

   /* Invoking the default notification service */
        NotificationCompat.Builder  mBuilder = new NotificationCompat.Builder(this);

        // What time to show on the notification
        long time = System.currentTimeMillis();

        mBuilder.setContentTitle("Reminder");
        mBuilder.setContentText("Have you enter your transaction today:)");
        mBuilder.setTicker("Alert!");
        mBuilder.setWhen(time);
        mBuilder.setSmallIcon(R.drawable.ic_alarm_white_48dp);
        mBuilder.setAutoCancel(true);






   /* Creates an explicit intent for an Activity in your app */
        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);


        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
//
//   /* Adds the Intent that starts the Activity to the top of the stack */
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =stackBuilder.getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);


        mBuilder.setContentIntent(resultPendingIntent);
        myNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

   /* notificationID allows you to update the notification later on. */
        myNotificationManager.notify(0, mBuilder.build());
    }
}
