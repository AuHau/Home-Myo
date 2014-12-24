package cz.cvut.uhlirad1.homemyo.knx.cat;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.ICommandParser;
import cz.cvut.uhlirad1.homemyo.knx.KnxDataTypeEnum;
import cz.cvut.uhlirad1.homemyo.knx.KnxElementTypes;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 7.12.14
 */
public class DummyCommandParser implements ICommandParser {
    @Override
    public List<Command> parse() {

        LinkedList<Command> commands = new LinkedList<Command>();
        commands.add(new Command(1, "Turn off all lights", "Example", "3/0/10", KnxDataTypeEnum.BOOL, KnxElementTypes.CENTRAL_FUNCTION));
        commands.add(new Command(2, "Lights in 304a", "Example", "3/1/11", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.add(new Command(3, "Lights in 304d", "Example", "3/1/2", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.add(new Command(4, "Lights in 304c", "Example", "3/1/1", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.add(new Command(5, "Lights in 304b", "Example", "3/1/6", KnxDataTypeEnum.BOOL, KnxElementTypes.LIGHT));
        commands.add(new Command(6, "Doors in 304", "Example", "3/2/1", KnxDataTypeEnum.BOOL, KnxElementTypes.BLINDS));
        return commands;
    }
}
