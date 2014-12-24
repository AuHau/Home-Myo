package cz.cvut.uhlirad1.homemyo.knx;

import android.content.Context;
import cz.cvut.uhlirad1.homemyo.knx.cat.XMLCommandParser_;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 8.12.14
 */
public final class CommandParserFactory {
    private CommandParserFactory() {
    }

    public static final ICommandParser createCommandParser(Context context){
        return XMLCommandParser_.getInstance_(context);
    }
}
