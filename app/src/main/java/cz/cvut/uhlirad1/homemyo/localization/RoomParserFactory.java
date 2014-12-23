package cz.cvut.uhlirad1.homemyo.localization;

import android.content.Context;
import cz.cvut.uhlirad1.homemyo.localization.cat.XMLRoomParser_;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public final class RoomParserFactory {

    private RoomParserFactory(){}

    public static IRoomParser createParser(Context context){
        return XMLRoomParser_.getInstance_(context);
    }

}
