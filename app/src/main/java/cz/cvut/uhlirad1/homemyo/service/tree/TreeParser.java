package cz.cvut.uhlirad1.homemyo.service.tree;

import android.util.Log;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 19.12.14
 */
public class TreeParser {

    private Map<Integer, Command> commands;

    public TreeParser(Map<Integer, Command> commands) {
        this.commands = commands;
    }

    public List<Room> parse(File config){
        Serializer serializer = new Persister();

        Rooms rooms = null;
        try {
            rooms = serializer.read(Rooms.class, config);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rooms.getRoom();
    }

    public void save(File config, List<Room> rooms) {
        if (!config.exists()) {
            // TODO: Error Handling
            Log.e("TreeParser", "Tree config doesn't exist!");
            return;
        }

        Serializer serializer = new Persister();
        try {
            serializer.write(rooms, config);
        } catch (Exception e) {
            // TODO: Error Handling
            Log.e("TreeParser", "Saving Tree was unsuccessful!");
            e.printStackTrace();
        }
    }
}
