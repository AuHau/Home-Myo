package cz.cvut.uhlirad1.homemyo.knx;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * Interface which specifies Command parser.
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
public interface ICommandParser {
    /**
     * Method will parse file config, which should contain Commands definition.
     * @param config
     * @return
     * @throws Exception
     */
    public Map<Integer, Command> parse(File config) throws Exception;

    /**
     * Method will save Commands definied in commands variable into File config.
     * @param config
     * @param commands
     * @throws Exception
     */
    public void save(File config, Map<Integer, Command> commands) throws Exception;
}
