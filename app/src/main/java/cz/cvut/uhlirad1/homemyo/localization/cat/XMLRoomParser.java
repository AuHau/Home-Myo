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
@EBean
public class XMLRoomParser implements IRoomParser {

    @Pref
    AppPreferences_ preferences;

    List<Room> rooms;

    Map<String, Room> mapping;

    /**
     * Initialization method which will parse data from XML config
     * defined in AppPreferences.
     */
    @AfterInject
    public void init() {
        File configDir = new File(Environment.getExternalStorageDirectory() + File.separator + preferences.applicationFolder().get());

        if(!configDir.exists() && !configDir.mkdirs()){
            // TODO: Error Handling
            Log.e("XMLRoomParser", "Config directory does not exist and can not be created!");
            return;
        }

        File config = new File (configDir, preferences.roomConfig().get());
        if (!config.exists()) {
            // TODO: Error Handling
            Log.e("XMLRoomParser", "Rooms config can not be found!");
            return;
        }

        Serializer serializer = new Persister();
        try {
            Rooms roomsContainer = serializer.read(Rooms.class, config);
            rooms = roomsContainer.getRoom();
            createMapping();
        }catch (XMLRoomParserException e){
            // TODO: Error Handling
            Log.e("XMLRoomParser", "Not valid Rooms config data!");

        } catch (Exception e) {
            // TODO: Error Handling
            Log.e("XMLRoomParser", "Parsing of Rooms was unsuccessful!");
            e.printStackTrace();
        }
    }

    /**
     * Method which will create Mapping from parsed List of Rooms.
     * It will also check consistency of the parsed data, if some error
     * will be found, it will throw XMLRoomParserException
     *
     * @throws XMLRoomParserException
     */
    private void createMapping() throws XMLRoomParserException {
        mapping = new HashMap<String, Room>();
        Set<Integer> idSet = new HashSet<Integer>();

        for (Room room : rooms) {
            if(room.getName().isEmpty() || room.getMapping().isEmpty() || room.getId() == 0){
                throw new XMLRoomParserException("Rooms config has not valid data! Room with ID - " + room.getId());
            }

            if (idSet.contains(room.getId())) {
                throw new XMLRoomParserException("There is Room which has assigned ID which was already assigned before. ID - " + room.getId());
            }

            idSet.add(room.getId());
            mapping.put(room.getMapping(), room);
        }

        rooms.add(0, new Room(0, "Smart Home"));
    }

    @Override
    public List<Room> parse() {
        return rooms;
    }

    @Override
    public Map<String, Room> parseMapping() {
        return mapping;
    }
}
