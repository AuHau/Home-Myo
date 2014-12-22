package cz.cvut.uhlirad1.homemyo.localization;

import cz.cvut.uhlirad1.homemyo.localization.cat.DummyRoomsParser;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public final class RoomsParserFactory {

    private RoomsParserFactory(){}

    public static IRoomsParser createParser(){
        return new DummyRoomsParser();
    }

}
