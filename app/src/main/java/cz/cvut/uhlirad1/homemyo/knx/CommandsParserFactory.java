package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.knx.cat.DummyCommandsParser;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 8.12.14
 */
public final class CommandsParserFactory {
    private CommandsParserFactory() {
    }

    public static final ICommandsParser createCommandsParser(){
        return new DummyCommandsParser();
    }
}
