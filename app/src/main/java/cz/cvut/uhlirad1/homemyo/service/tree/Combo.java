package cz.cvut.uhlirad1.homemyo.service.tree;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created on 24.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
@Root
public class Combo {

    @Attribute
    private int id;

    @Attribute(name = "command_id")
    private int commandId;

    @ElementList(inline = true)
    private List<MyoPose> myoPose;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCommandId() {
        return commandId;
    }

    public void setCommandId(int commandId) {
        this.commandId = commandId;
    }

    public List<MyoPose> getMyoPose() {
        return myoPose;
    }

    public void setMyoPose(List<MyoPose> myoPose) {
        this.myoPose = myoPose;
    }
}
