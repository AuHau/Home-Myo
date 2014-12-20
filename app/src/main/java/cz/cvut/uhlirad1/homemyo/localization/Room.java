package cz.cvut.uhlirad1.homemyo.localization;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 1.12.14
 */

@Root
public final class Room{
    @Attribute
    private int id;

    @Element(required = false)
    private String name;

    @Element(required = false)
    private int order;

    @ElementList(required = false, inline = true)
    private List<Command> command;

    public Room() {
    }

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        order = id;
        command = null;
    }

    public Room(int id, String name, int order, List<Command> command) {
        this.id = id;
        this.name = name;
        this.order = order;
        this.command = command;
    }

    public int getOrder() {
        return order;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Command> getCommand() {
        return command;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public void setCommand(List<Command> command) {
        this.command = command;
    }
}
