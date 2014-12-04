package cz.cvut.uhlirad1.homemyo.knx;

import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 4.12.14
 */
public interface ICommandsParser {
    public List<Command> parse();
}
