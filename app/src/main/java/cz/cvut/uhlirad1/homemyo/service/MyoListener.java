package cz.cvut.uhlirad1.homemyo.service;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.R;
import cz.cvut.uhlirad1.homemyo.settings.SettingsKeys;
import cz.cvut.uhlirad1.homemyo.knx.*;
import cz.cvut.uhlirad1.homemyo.localization.*;
import cz.cvut.uhlirad1.homemyo.service.tree.Node;
import cz.cvut.uhlirad1.homemyo.service.tree.TreeParser;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 17.12.14
 */
public class MyoListener extends AbstractDeviceListener {

    private IAdapter adapter;

    private ITracker tracker;

    private Map<Integer, Node> trees;

    private boolean isLocalizationEnabled;

    private long lockingTime;

    //////////////////////////////////

    private long comboStartTimestamp;

    private Node actualRoomNode;

    private Node actualAllNode;

    private LockingRunnable currentLock;

    private boolean locked;

    private Handler handler ;


    public MyoListener(Context context) {

        locked = true;
        handler = new Handler();

        File config = new File(context.getExternalFilesDir(null), "tree.xml");

        List<Command> commands = CommandsParserFactory.createCommandsParser().parse();

        TreeParser treeParser = new TreeParser(commands);
        trees = treeParser.parse(config);

        adapter = AdapterFactory.createAdapter(context);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Resources resources = context.getResources();

        isLocalizationEnabled = preferences.getBoolean(SettingsKeys.LOC_ENABLED.toString(),
                resources.getBoolean(R.bool.default_loc_enabled));

        // TODO: Opravit preference - možná zkusit použít AA?
//        lockingTime = preferences.getInt(SettingsKeys.LOCK_TIME.toString(),
//                resources.getInteger(R.integer.default_lock_time)) * 1000;
        lockingTime = resources.getInteger(R.integer.default_lock_time) * 1000;
//        lockingTime = preferences.getInt("LOCK_TIME", 2) * 1000;

        if (isLocalizationEnabled)
            tracker = TrackerFactory.createTracker(context, RoomsParserFactory.createHomeParser());
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
            if (isLocalizationEnabled) {
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
            // TODO: Přidat možnost způsobu potvrzování comb - Double tapem / timeoutem
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
        // TODO: Přetypování podle typu v Commandu a né brutefore na Boolean
        telegram.setBoolean((Boolean) adapter.getState(telegram));
        adapter.sendTelegram(telegram);
    }

    private void unlock(Myo myo) {
        Log.d("MyoListener", "Unlocked Myo");
        locked = false;
        vibrateUnlock(myo);

        currentLock = new LockingRunnable(myo);
        handler.postDelayed(currentLock, lockingTime);
    }

    private void postponeLock(){
        if(currentLock == null) return;
        handler.removeCallbacks(currentLock);
        handler.postDelayed(currentLock, lockingTime);
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
