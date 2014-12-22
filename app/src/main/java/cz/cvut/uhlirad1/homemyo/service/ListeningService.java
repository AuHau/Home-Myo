package cz.cvut.uhlirad1.homemyo.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import com.thalmic.myo.*;
import cz.cvut.uhlirad1.homemyo.MainActivity;
import cz.cvut.uhlirad1.homemyo.R;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 8.12.14
 */
public class ListeningService extends Service {

    private static final String TAG = "ListeningService";
    private Toast mToast;
    private DeviceListener mListener;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;

    @Override
    public void onCreate() {
        super.onCreate();

        HandlerThread thread = new HandlerThread("MyoListener",
                Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        showToast("Watching for Myo gestures");

        makeNotification();

        Message msg = mServiceHandler.obtainMessage();
        msg.arg1 = startId;
        mServiceHandler.sendMessage(msg);

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        showToast("Stop watching for Myo gestures");

        Hub.getInstance().removeListener(mListener);
        Hub.getInstance().shutdown();

        destroyNotification();
    }

    private void makeNotification(){
        Intent notifyIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, notifyIntent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle("Home Myo is running")
                .setContentText("We are watching for your gestures!")
                .setSmallIcon(R.drawable.ic_stat_notify)
                .setAutoCancel(false)
                .setContentIntent(pIntent)
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_LOW)
                .build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(0, n);
    }

    private void destroyNotification(){
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(0);
    }


    private void showToast(String text) {
        Log.w(TAG, text);
        if (mToast == null) {
            mToast = Toast.makeText(this, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
        }
        mToast.show();
    }

    private final class ServiceHandler extends Handler {

        private Context context;

        public ServiceHandler(Looper looper, Context context) {
            super(looper);
            this.context = context;
        }

        @Override
        public void handleMessage(Message msg) {

            Hub hub = Hub.getInstance();
            if (!hub.init(context, getPackageName())) {
                showToast("Couldn't initialize Hub");
                stopSelf();
                return;
            }

            hub.setLockingPolicy(Hub.LockingPolicy.NONE);

            // TODO: Notifikace pro uživatele když neni Myo synchronizováno
            mListener = new MyoListener(context);
            hub.addListener(mListener);

            // TODO: Automatický connect k známému Myo (ukládání zpárovaného Myo; ukládat posledního známého)
            // TODO: Automatický connect jenom v případě, že jsem na domácí WiFi
            if (hub.getConnectedDevices().isEmpty()) {
//                String myoAddress = PreferenceManager.getDefaultSharedPreferences(context).getString("myo_mac", "");
                // If we have a saved Myo MAC address then connect to it, otherwise look for one nearby.
//                if (TextUtils.isEmpty(myoAddress)) {
//                hub.attachToAdjacentMyo();
//                } else {
//                    hub.attachByMacAddress(myoAddress);
                    hub.attachByMacAddress("CE:CD:53:B4:7C:DA");
//                }
            }
        }
    }
}