package cz.cvut.uhlirad1.homemyo.knx;

import android.content.Context;
import cz.cvut.uhlirad1.homemyo.knx.cat.XMLCommandParser_;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 8.12.14
 */
public final class CommandsParserFactory {
    private CommandsParserFactory() {
    }

    public static final ICommandsParser createCommandsParser(Context context){
        return XMLCommandParser_.getInstance_(context);
    }
}
