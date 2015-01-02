package cz.cvut.uhlirad1.homemyo.knx;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 4.12.14
 */
public interface ICommandParser {
    public Map<Integer, Command> parse(File config) throws Exception;

    public void save(File config, Map<Integer, Command> commands) throws Exception;
}
