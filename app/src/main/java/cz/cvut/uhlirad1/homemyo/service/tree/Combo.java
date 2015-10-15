package cz.cvut.uhlirad1.homemyo.service.tree;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
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

    @Element(required = false)
    private String name;

    @ElementList(inline = true, name = "myo-pose")
    private List<MyoPose> myoPose;

    public Combo() {
    }

    public Combo(int id, int commandId, String name, List<MyoPose> myoPose) {
        this.id = id;
        this.commandId = commandId;
        this.name = name;
        this.myoPose = myoPose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
