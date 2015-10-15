package cz.cvut.uhlirad1.homemyo.localization.cat;

import cz.cvut.uhlirad1.homemyo.localization.IRoomParser;
import cz.cvut.uhlirad1.homemyo.localization.Room;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adam on 1.12.14.
 */
public class DummyRoomParser implements IRoomParser {

    private HashMap<String, Room> mapping;
    private Map<Integer, Room> list;

    public DummyRoomParser() {
        list = new HashMap<Integer, Room>();
        Room r0 = new Room(0, "Celý byt");
        Room r1 = new Room(1, "Zasedačka (304a)");
        Room r2 = new Room(2, "Kuchyně (304b)");
        Room r3 = new Room(3, "Pokoj (304c)");
        Room r4 = new Room(4, "Koupelna (304d)");
        Room r5 = new Room(5, "Chodba");


        list.put(r0.getId(), r0);
        list.put(r1.getId(), r1);
        list.put(r2.getId(), r2);
        list.put(r3.getId(), r3);
        list.put(r4.getId(), r4);
        list.put(r5.getId(), r5);


        mapping = new HashMap<String, Room>();
        mapping.put("304a", r1);
        mapping.put("304b", r2);
        mapping.put("304c", r3);
        mapping.put("304d", r4);
        mapping.put("304d", r4);
        mapping.put("chodba", r5);

    }

    @Override
    public Map<Integer, Room> parse(File file){
        return list;
    }

    @Override
    public Map parseMapping() {
        return mapping;
    }

    @Override
    public void save(File config, Map<Integer, Room> rooms) {

    }
}
