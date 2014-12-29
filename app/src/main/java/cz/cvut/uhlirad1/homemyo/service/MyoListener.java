package cz.cvut.uhlirad1.homemyo.service;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.AppData;
import cz.cvut.uhlirad1.homemyo.knx.*;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatAdapter;
import cz.cvut.uhlirad1.homemyo.localization.ITracker;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.localization.TrackerException;
import cz.cvut.uhlirad1.homemyo.localization.TrackerFactory;
import cz.cvut.uhlirad1.homemyo.service.tree.Node;
import cz.cvut.uhlirad1.homemyo.service.tree.TreeParser;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 17.12.14
 */
@EBean
public class MyoListener extends AbstractDeviceListener {

    private IAdapter adapter;

    private ITracker tracker;

    private Map<Integer, Node> trees;

    @Pref
    protected AppPreferences_ preferences;

    @Bean
    protected AppData data;

    @RootContext
    protected Context context;

    //////////////////////////////////

    private Node actualRoomNode;

    private Node actualAllNode;

    private LockingRunnable currentLock;

    private boolean locked;

    private Handler handler ;

    @AfterInject
    public void init() {
        locked = true;
        handler = new Handler();

        trees = data.getRootTree();

        adapter = AdapterFactory.createAdapter(context);

        tracker = TrackerFactory.createTracker(context);
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        // TODO: Krátký sleep po udělání gesta? Je to nutný?
        // Skip rest pose
        if (pose == Pose.REST) return;
        
        Log.d("MyoListener", "Pose detected - " + pose);

        // Unlocking mechanism
        if (actualAllNode == null && actualRoomNode == null && locked && pose == Pose.DOUBLE_TAP) {
            unlock(myo);
            return;
        }

        // If Myo is locked ignore poses
        if(locked) return;

        // Unknown pose will reset searching tree
        if (pose == Pose.UNKNOWN) {
            Log.e("MyoListener", "Unknown pose detected");
            vibrateError(myo);
            lock(myo, false);
            return;
        }

        // Pose detected, prolonging locking time
        postponeLock();

        // New combo
        if (actualAllNode == null && actualRoomNode == null) {
            if (preferences.locEnabled().get()) {
                try {
                    Room room = tracker.getLocation();
                    actualRoomNode = trees.get(room.getId());
                } catch (TrackerException e) {
                    // No location was found
                    Log.e("MyoListener", "No location was found");
                    vibrateError(myo);
                    lock(myo, false);
                }
            }

            actualAllNode = trees.get(0);
        } else if (pose == Pose.DOUBLE_TAP) { // Combo is confirmed, perform command if it is bind
            if ((actualRoomNode != null && actualRoomNode.hasCommand()) ||
                    (actualAllNode!= null && actualAllNode.hasCommand())) {

                Command commandToPerform;
                if(actualRoomNode != null && actualRoomNode.hasCommand())
                    commandToPerform = actualRoomNode.getCommand();
                else
                    commandToPerform = actualAllNode.getCommand();
                sendCommand(commandToPerform);

                Log.i("MyoListener", "Combo detected, command '" + commandToPerform.getName() + "' will be executed");
                vibrateConfirmationCombo(myo);
                lock(myo, false);
            } else {
                Log.d("MyoListener", "Confirmed combo doesn't have bind command");
                vibrateCommandNotFound(myo);
                lock(myo, false);
            }

            return; // Double tap ends combo
        }

        // Combo is in process, move to another Node
        if(actualRoomNode != null)
            actualRoomNode = actualRoomNode.getChild(pose);
        if(actualAllNode != null)
            actualAllNode = actualAllNode.getChild(pose);

        if (actualRoomNode == null && actualAllNode == null) { // Pose not in tree, command not found - reset combo
            Log.d("MyoListener", "Performed pose is not leading to any command");
            vibrateCommandNotFound(myo);
            lock(myo, false);
        } else {
            Log.d("MyoListener", "Pose found");
            vibrateConfirmationPose(myo);
        }
    }

    private void sendCommand(Command command) {
        ITelegram telegram = TelegramFactory.createTelegram();
        telegram.setCommand(command);
//        telegram.setBoolean((Boolean) adapter.getState(telegram));
        adapter.sendTelegram(telegram);
    }

    private void unlock(Myo myo) {
        Log.d("MyoListener", "Unlocked Myo");
        locked = false;
        vibrateUnlock(myo);

        currentLock = new LockingRunnable(myo);
        handler.postDelayed(currentLock, preferences.lockTimeout().get() * 1000);
    }

    private void postponeLock(){
        if(currentLock == null) return;
        handler.removeCallbacks(currentLock);
        handler.postDelayed(currentLock, preferences.lockTimeout().get() * 1000);
    }

    private void lock(Myo myo){
        lock(myo, true);
    }

    private void lock(Myo myo, boolean shouldVibrate){
        Log.d("MyoListener", "Locked Myo");
        handler.removeCallbacks(currentLock);
        currentLock = null;
        locked = true;
        actualRoomNode = null;
        actualAllNode = null;
        if (shouldVibrate) vibrateLock(myo);
    }

    private void vibrateLock(Myo myo) {
        myo.vibrate(Myo.VibrationType.MEDIUM);
    }

    private void vibrateUnlock(Myo myo) {
        myo.vibrate(Myo.VibrationType.MEDIUM);
    }

    private void vibrateCommandNotFound(Myo myo) {
        myo.vibrate(Myo.VibrationType.SHORT);
        myo.vibrate(Myo.VibrationType.SHORT);
        myo.vibrate(Myo.VibrationType.SHORT);
    }

    private void vibrateError(Myo myo) {
        myo.vibrate(Myo.VibrationType.SHORT);
        myo.vibrate(Myo.VibrationType.SHORT);
        myo.vibrate(Myo.VibrationType.SHORT);
        myo.vibrate(Myo.VibrationType.SHORT);
        myo.vibrate(Myo.VibrationType.SHORT);
    }

    private void vibrateConfirmationPose(Myo myo) {
        myo.vibrate(Myo.VibrationType.SHORT);
    }

    private void vibrateConfirmationCombo(Myo myo) {
        myo.vibrate(Myo.VibrationType.LONG);
    }

    private class LockingRunnable implements Runnable{

        private Myo myo;

        public LockingRunnable(Myo myo) {
            this.myo = myo;
        }

        @Override
        public void run() {
            if(currentLock != null){
                lock(myo);
            }
        }
    }
}
