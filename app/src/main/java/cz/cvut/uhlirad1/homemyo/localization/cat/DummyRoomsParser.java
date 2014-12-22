package cz.cvut.uhlirad1.homemyo.localization.cat;

import cz.cvut.uhlirad1.homemyo.localization.IRoomsParser;
import cz.cvut.uhlirad1.homemyo.localization.Room;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by adam on 1.12.14.
 */
// TODO: Implementovat XML parser
public class DummyRoomsParser implements IRoomsParser {

    private HashMap<String, Room> mapping;
    private List<Room> list;

    public DummyRoomsParser() {
        list = new ArrayList<Room>(5);
        Room r0 = new Room(0, "Celý byt");
        Room r1 = new Room(1, "Zasedačka (304a)");
        Room r2 = new Room(2, "Kuchyně (304b)");
        Room r3 = new Room(3, "Pokoj (304c)");
        Room r4 = new Room(4, "Koupelna (304d)");
        Room r5 = new Room(5, "Chodba");


        list.add(r0.getId(), r0);
        list.add(r1.getId(), r1);
        list.add(r2.getId(), r2);
        list.add(r3.getId(), r3);
        list.add(r4.getId(), r4);
        list.add(r5.getId(), r5);


        mapping = new HashMap<String, Room>();
        mapping.put("304a", r1);
        mapping.put("304b", r2);
        mapping.put("304c", r3);
        mapping.put("304d", r4);
        mapping.put("304d", r4);
        mapping.put("chodba", r5);

    }

    @Override
    public List<Room> parse(){
        return list;
    }

    @Override
    public HashMap parseMapping() {
        // TODO: Přepsat tak, aby nepadalo v případě že předtím nebylo zavoláno parse()
        return mapping;
    }
}
