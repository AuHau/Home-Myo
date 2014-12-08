package cz.cvut.uhlirad1.homemyo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.widget.Toast;
import cz.cvut.uhlirad1.homemyo.MainActivity;
import cz.cvut.uhlirad1.homemyo.R;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 8.12.14
 */
public class ListeningService extends Service {
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }
        @Override
        public void handleMessage(Message msg) {
            long endTime = System.currentTimeMillis() + 5*1000;
            while (System.currentTimeMillis() < endTime) {
                synchronized (this) {
                    try {
                        wait(endTime - System.currentTimeMillis());
                    } catch (Exception e) {
                    }
                }
            }

            stopSelf(msg.arg1);
        }
    }

    private void makeNotification(){
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Home Myo is running")
                .setContentText("We are watching for your gestures!")
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setContentIntent(pIntent)
                .setAutoCancel(false)
                .build();
        n.flags = Notification.FLAG_ONGOING_EVENT;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);
    }

    private void destroyNotification(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }

    @Override
    public void onCreate() {
        HandlerThread thread = new HandlerThread("ServiceStartArguments",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();

        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Watching for Myo gestures", Toast.LENGTH_LONG).show();

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        makeNotification();

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Stop watching for Myo gestures", Toast.LENGTH_LONG).show();

        destroyNotification();
    }
}