package cz.cvut.uhlirad1.homemyo.localization;

import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 1.12.14
 */
public final class Home {
    private static List<Room> rooms;

    public static List<Room> getRooms(){
        if(rooms == null){
            parseRooms();
        }

        return rooms;
    }

    private static void parseRooms() {
        IRoomsParser parser = RoomsParserFactory.createParser();
        rooms = parser.parse();
    }
}
