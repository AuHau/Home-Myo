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

    public List<Room> parse(File config) throws Exception {
        Serializer serializer = new Persister();

        Rooms rooms = null;
        try {
            rooms = serializer.read(Rooms.class, config);
        } catch (Exception e) {
            Log.e("TreeParser", "Parsing Tree config was unsuccessful!");
            throw new Exception("Tree configuration file was not possible to save.");
        }

        return rooms.getRoom();
    }

    public void save(File config, List<Room> rooms) throws Exception {
        Rooms rooms1 = new Rooms();
        rooms1.setRoom(rooms);
        Serializer serializer = new Persister();
        try {
            serializer.write(rooms1, config);
        } catch (Exception e) {
            Log.e("TreeParser", "Saving Tree config was unsuccessful!");
            throw new Exception("Tree configuration file was not possible to save.");
        }
    }
}
