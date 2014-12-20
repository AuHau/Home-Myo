package cz.cvut.uhlirad1.homemyo.service;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.thalmic.myo.AbstractDeviceListener;
import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.knx.AdapterFactory;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.CommandsParserFactory;
import cz.cvut.uhlirad1.homemyo.knx.IAdapter;
import cz.cvut.uhlirad1.homemyo.localization.ITracker;
import cz.cvut.uhlirad1.homemyo.localization.RoomsParserFactory;
import cz.cvut.uhlirad1.homemyo.localization.TrackerFactory;
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

    public MyoListener(Context context) {
        super();

        File config = new File(context.getExternalFilesDir(null), "tree.xml");

        List<Command> commands = CommandsParserFactory.createCommandsParser().parse();

        TreeParser treeParser = new TreeParser(commands);
        trees = treeParser.parse(config);

        adapter = AdapterFactory.createAdapter(context);

        tracker = TrackerFactory.createTracker(context, RoomsParserFactory.createHomeParser());
    }

    @Override
    public void onPose(Myo myo, long timestamp, Pose pose) {

    }
}
