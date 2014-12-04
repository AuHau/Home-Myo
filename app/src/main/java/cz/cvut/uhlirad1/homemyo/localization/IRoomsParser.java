package cz.cvut.uhlirad1.homemyo.localization;

import java.util.HashMap;
import java.util.List;

/**
 * Created by adam on 3.12.14.
 */
public interface IRoomsParser {
    /**
     * Method will parse Rooms specification and return List of Rooms
     * @return
     */
    public List<Room> parse();

    /**
     * Since Localization can be implemented several ways, mapping of
     * received localization information to internal structure of Rooms
     * needs to be done. This method will give mapping HashMap, where Key
     * is received locality information from Localization service and Value
     * is instance of internal Room class.
     *
     * @return
     */
    public HashMap parseMapping();
}
