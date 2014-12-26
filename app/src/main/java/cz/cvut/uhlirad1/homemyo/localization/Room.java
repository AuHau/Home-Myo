package cz.cvut.uhlirad1.homemyo.localization;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.service.tree.Combo;
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

    @ElementList(required = false, inline = true)
    private List<Combo> combo;

    @Element(required = false)
    private String mapping;

    public Room() {
    }

    public Room(int id) {
        this.id = id;
    }

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        combo = null;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public List<Combo> getCombo() {
        return combo;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCombo(List<Combo> combo) {
        this.combo = combo;
    }

    public String getMapping() {
        return mapping;
    }

    public void setMapping(String mapping) {
        this.mapping = mapping;
    }
}
