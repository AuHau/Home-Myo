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
 * Class for manipulating Tree configuration file.
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
public class TreeParser {

    /**
     * Method will parse XML file from file specified in config. Structure of the config
     * file has to be same as defined in {@link cz.cvut.uhlirad1.homemyo.service.tree.Rooms} class.
     * If parsing was unsuccessful Exception is thrown.
     *
     * @param config
     * @return
     * @throws Exception
     */
    public List<Room> parse(File config) throws Exception {
        Serializer serializer = new Persister();

        Rooms rooms = null;
        try {
            rooms = serializer.read(Rooms.class, config);
        } catch (Exception e) {
            Log.e("TreeParser", "Parsing Tree config was unsuccessful! " + e.getMessage());
            throw new Exception("Tree configuration file was not possible to save.");
        }

        return rooms.getRoom();
    }

    /**
     * Method will save Trees defined in rooms to file specified in config.
     * The XML structure is defined in {@link cz.cvut.uhlirad1.homemyo.service.tree.Rooms} class.
     * If saving the XML was unsuccessful Exception is thrown.
     *
     * @param config
     * @param rooms
     * @throws Exception
     */
    public void save(File config, List<Room> rooms) throws Exception {
        Rooms rooms1 = new Rooms();
        rooms1.setRoom(rooms);
        Serializer serializer = new Persister();
        try {
            serializer.write(rooms1, config);
        } catch (Exception e) {
            Log.e("TreeParser", "Saving Tree config was unsuccessful! " + e.getMessage());
            throw new Exception("Tree configuration file was not possible to save.");
        }
    }
}
