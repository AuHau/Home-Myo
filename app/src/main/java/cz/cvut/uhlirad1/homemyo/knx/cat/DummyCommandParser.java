package cz.cvut.uhlirad1.homemyo.knx.cat;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.ICommandParser;
import cz.cvut.uhlirad1.homemyo.knx.KnxDataTypeEnum;
import cz.cvut.uhlirad1.homemyo.knx.KnxElementTypes;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 7.12.14
 */
public class DummyCommandParser implements ICommandParser {
    @Override
    public Map<Integer, Command> parse(File file) {

        Map<Integer, Command> commands = new HashMap<Integer, Command>();
        commands.put(1, new Command(1, "Turn off all lights", "Example", "3/0/10", KnxDataTypeEnum.BOOL, KnxElementTypes.CENTRAL_FUNCTION));
        commands.put(2, new Command(2, "Lights in 304a", "Example", "3/1/11", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.put(3, new Command(3, "Lights in 304d", "Example", "3/1/2", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.put(4, new Command(4, "Lights in 304c", "Example", "3/1/1", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.put(5, new Command(5, "Lights in 304b", "Example", "3/1/6", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.put(6, new Command(6, "Doors in 304", "Example", "3/2/1", KnxDataTypeEnum.BOOL, KnxElementTypes.BLINDS));
        return commands;
    }

    @Override
    public void save(File config, Map<Integer, Command> commands) {

    }
}
