package cz.cvut.uhlirad1.homemyo.service;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.R;
import cz.cvut.uhlirad1.homemyo.SettingsKeys;
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

    //////////////////////////////////

    private long comboStartTimestamp;

    private Node actualNode;

    public MyoListener(Context context) {
        super();

        File config = new File(context.getExternalFilesDir(null), "tree.xml");

        List<Command> commands = CommandsParserFactory.createCommandsParser().parse();

        TreeParser treeParser = new TreeParser(commands);
        trees = treeParser.parse(config);

        adapter = AdapterFactory.createAdapter(context);

        isLocalizationEnabled = PreferenceManager.getDefaultSharedPreferences(context)
                .getBoolean(SettingsKeys.LOC_ENABLED.toString(),
                        context.getResources().getBoolean(R.bool.default_loc_enabled));

        if (isLocalizationEnabled)
            tracker = TrackerFactory.createTracker(context, RoomsParserFactory.createHomeParser());
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {
        Log.d("MyoListener", "Pose detected - " + pose);

        // Unknown pose will reset searching tree
        if (pose == Pose.UNKNOWN) {
            actualNode = null;
//            vibrateError(myo);
        }

        // Skip rest pose
        if (pose == Pose.REST) return;

        // New combo
        if (actualNode == null) {
            if (isLocalizationEnabled) {
                try {
                    Room room = tracker.getLocation();
                    actualNode = trees.get(room.getId());
                } catch (TrackerException e) {
                    // No location was found
//                    vibrateError(myo);
                }
            } else {
                actualNode = trees.get(0);
            }
        } else if (pose == Pose.DOUBLE_TAP) { // Combo is confirmed, perform command if it is bind
            if (actualNode.hasCommand()) {
                Log.i("MyoListener", "Combo detected, command " + actualNode.getCommand().getName() + " will be executed");
                ITelegram telegram = TelegramFactory.createTelegram();
                telegram.setCommand(actualNode.getCommand());
                // TODO: Musím zjistit jaká je současná hodnota, abych poslal tu druhou
                telegram.setBoolean(true);

//            adapter.sendTelegram(telegram);
//            vibrateConfirmationCombo(myo);
            } else {
                vibrateCommandNotFound(myo);
            }
        }else{ // Combo is in process, move to another Node
            actualNode = actualNode.getChild(pose);
            if (actualNode == null) { // Pose not in tree, command not found - reset combo
                vibrateCommandNotFound(myo);
            } else {
                vibrateConfirmationPose(myo);
            }
        }
    }

    @Override
    public void onUnlock(Myo myo, long timestamp) {
        Log.d("MyoListener", "Unlocked Myo");
//        vibrateUnlock(myo);
    }

    @Override
    public void onLock(Myo myo, long timestamp) {
        Log.d("MyoListener", "Locked Myo");
//        vibrateLock(myo);
    }

    private void vibrateLock(Myo myo) {
        myo.vibrate(Myo.VibrationType.MEDIUM);
        myo.vibrate(Myo.VibrationType.SHORT);
    }

    private void vibrateUnlock(Myo myo) {
        myo.vibrate(Myo.VibrationType.SHORT);
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
        myo.vibrate(Myo.VibrationType.SHORT);
    }
}
