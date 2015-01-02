package cz.cvut.uhlirad1.homemyo.localization.cat;

import android.os.Environment;
import android.util.Log;
import cz.cvut.uhlirad1.homemyo.localization.IRoomParser;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.service.tree.Rooms;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.*;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 23.12.14
 */
public class XMLRoomParser implements IRoomParser {

    Map<Integer, Room> rooms;

    Map<String, Room> mapping;

    /**
     * Method which will transform parsed List of Rooms into Map and
     * create Mapping from parsed List of Rooms.
     * It will also check consistency of the parsed data, if some error
     * will be found, it will throw XMLRoomParserException
     *
     * @throws XMLRoomParserException
     */
    private void createMapping(List<Room> list) throws XMLRoomParserException {
        mapping = new HashMap<String, Room>();
        rooms = new HashMap<Integer, Room>();
        Set<Integer> idSet = new HashSet<Integer>();

        for (Room room : list) {
            if(room.getName().isEmpty() || room.getMapping().isEmpty() || room.getId() == 0){
                throw new XMLRoomParserException("Rooms config has not valid data! Room with ID - " + room.getId());
            }

            if (idSet.contains(room.getId())) {
                throw new XMLRoomParserException("There is Room which has assigned ID which was already assigned before. ID - " + room.getId());
            }

            idSet.add(room.getId());
            mapping.put(room.getMapping(), room);
            rooms.put(room.getId(), room);
        }

        rooms.put(0, new Room(0, "Smart Home"));
    }

    @Override
    public Map<Integer, Room> parse(File config) throws Exception {
        if (!config.exists()) {
            Log.e("XMLRoomParser", "Rooms config doesn't exist!");
            throw new XMLRoomParserException("Rooms configuration file can not be found!");
        }

        Serializer serializer = new Persister();
        Rooms roomsContainer = serializer.read(Rooms.class, config);
        List<Room> roomList = roomsContainer.getRoom();
        createMapping(roomList);
        return rooms;
    }

    @Override
    public Map<String, Room> parseMapping() {
        return mapping;
    }

    @Override
    public void save(File config, Map<Integer, Room> rooms) throws Exception {
        if (!config.exists()) {
            Log.e("XMLRoomParser", "Rooms config doesn't exist!");
            throw new XMLRoomParserException("Rooms configuration file can not be found!");
        }

        Serializer serializer = new Persister();
        Rooms commitRooms = new Rooms();
        commitRooms.setRoom(covertToList(rooms));
        serializer.write(commitRooms, config);
    }

    private List<Room> covertToList(Map<Integer, Room> rooms) {
        LinkedList<Room> list = new LinkedList<Room>();

        for (Map.Entry entryRoom: rooms.entrySet()) {
            list.add((Room) entryRoom.getValue());
        }

        return list;
    }
}
