package cz.cvut.uhlirad1.homemyo.service.tree;

import cz.cvut.uhlirad1.homemyo.localization.Room;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 20.12.14
 */
@Root
public class Rooms {

    @ElementList(inline = true)
    private List<Room> room;

    public Rooms(List<Room> room) {
        this.room = room;
    }

    public Rooms() {
    }

    public List<Room> getRoom() {
        return room;
    }

    public void setRoom(List<Room> room) {
        this.room = room;
    }
}
