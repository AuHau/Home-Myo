package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.knx.cat.XMLCommandParser;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 8.12.14
 */
public final class CommandParserFactory {
    private CommandParserFactory() {
    }

    public static final ICommandParser createCommandParser(){
        return new XMLCommandParser();
    }
}
