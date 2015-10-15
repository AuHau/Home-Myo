package cz.cvut.uhlirad1.homemyo.localization;

import cz.cvut.uhlirad1.homemyo.localization.cat.XMLRoomParser;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public final class RoomParserFactory {

    private RoomParserFactory(){}

    public static IRoomParser createParser(){
        return new XMLRoomParser();
    }

}
