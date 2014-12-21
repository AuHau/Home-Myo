package cz.cvut.uhlirad1.homemyo.knx.cat;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.ICommandsParser;
import cz.cvut.uhlirad1.homemyo.knx.KnxDataTypeEnum;
import cz.cvut.uhlirad1.homemyo.knx.KnxElementTypes;

import java.util.LinkedList;
import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 7.12.14
 */
public class DummyCommandsParser implements ICommandsParser {
    @Override
    public List<Command> parse() {

        LinkedList<Command> commands = new LinkedList<Command>();
        commands.add(new Command(1, "Example", "Example", "3/1/11", KnxDataTypeEnum.BOOLEAN, KnxElementTypes.LIGHT));
        commands.add(new Command(2, "Example", "Example", "3/1/11", KnxDataTypeEnum.BOOLEAN, KnxElementTypes.BLINDS));

        return commands;
    }
}
