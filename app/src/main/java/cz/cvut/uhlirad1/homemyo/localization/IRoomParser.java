package cz.cvut.uhlirad1.homemyo.localization;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by adam on 3.12.14.
 */
public interface IRoomParser {
    /**
     * Method will parse Rooms specification and return List of Rooms
     * @return
     */
    public Map<Integer, Room> parse(File config) throws Exception;

    /**
     * Since Localization can be implemented several ways, mapping of
     * received localization information to internal structure of Rooms
     * needs to be done. This method will give mapping HashMap, where Key
     * is received locality information from Localization service and Value
     * is instance of internal Room class.
     *
     * @return
     */
    public Map<String, Room> parseMapping();

    public void save(File config, Map<Integer,Room> rooms) throws Exception;
}
