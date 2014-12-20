package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
@Root
public class Command {

    @Attribute
    private int id;

    @Element(required = false)
    private String name;

    @Element(required = false)
    private String description;

    @Element(required = false)
    private String address;

    @Element(required = false)
    private KnxDataTypeEnum dataType;

    @Element(required = false)
    private KnxElementTypes elementType;

    @ElementList(required = false, inline = true)
    private List<MyoPose> myoPose;

    public Command() {
    }

    public Command(int id, List<MyoPose> myoPose) {
        this.id = id;
        this.myoPose = myoPose;
    }

    public Command(int id, String name, String description, String address, KnxDataTypeEnum dataType, KnxElementTypes elementTypes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.dataType = dataType;
        this.elementType = elementTypes;
    }

    public Command(int id, String name, String description, String address, KnxDataTypeEnum dataType, KnxElementTypes elementType, List<MyoPose> myoPose) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.dataType = dataType;
        this.elementType = elementType;
        this.myoPose = myoPose;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public KnxDataTypeEnum getDataType() {
        return dataType;
    }

    public KnxElementTypes getElementsType() {
        return elementType;
    }

    public String getAddress() {
        return address;
    }

    public List<MyoPose> getMyoPose() {
        return myoPose;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDataType(KnxDataTypeEnum dataType) {
        this.dataType = dataType;
    }

    public void setElementType(KnxElementTypes elementType) {
        this.elementType = elementType;
    }

    public void setMyoPose(List<MyoPose> myoPose) {
        this.myoPose = myoPose;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Command command = (Command) o;

        if (id != command.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}
