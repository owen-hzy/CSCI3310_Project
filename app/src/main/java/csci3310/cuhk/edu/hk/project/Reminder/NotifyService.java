package csci3310.cuhk.edu.hk.project.Reminder;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Binder;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.TimeZone;

import csci3310.cuhk.edu.hk.project.MainActivity;
import csci3310.cuhk.edu.hk.project.R;
import csci3310.cuhk.edu.hk.project.db.RecordsDataHelper;

/**
 * Created by csamyphew on 21/12/15.
 */
public class NotifyService extends IntentService {

    private RecordsDataHelper mDataHelper;

    public NotifyService() {
        super("ReminderService");
        mDataHelper = new RecordsDataHelper(this);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Calendar now = Calendar.getInstance(TimeZone.getDefault());
        int year = now.get(Calendar.YEAR);
        int month = now.get(Calendar.MONTH) + 1;
        int today = now.get(Calendar.DAY_OF_MONTH);
        String start = year + "-" + month + "-" + today;
        String end = year + "-" + month + "-" + (today + 1);
        Cursor cursor = mDataHelper.queryRaw("select * from record where timestamp between '" + start + "' and '" + end + "'");

        if (cursor == null || cursor.getCount() == 0) {
            NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            long when = System.currentTimeMillis();
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

            mBuilder.setContentTitle("Reminder");
            mBuilder.setContentText("Have you enter your transaction today:)");
            mBuilder.setTicker("Reminder");
            mBuilder.setWhen(when);
            mBuilder.setSmallIcon(R.drawable.ic_alarm_add);
            mBuilder.setAutoCancel(true);
            mBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
            mBuilder.setVibrate(new long[]{1000, 1000});
            mBuilder.setLights(Color.RED, 3000, 3000);

            Intent notificationIntent = new Intent(this, MainActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);

//   /* Adds the Intent that starts the Activity to the top of the stack */
            stackBuilder.addNextIntent(notificationIntent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            mBuilder.setContentIntent(resultPendingIntent);

            nm.notify(0, mBuilder.build());
        }
    }
}
