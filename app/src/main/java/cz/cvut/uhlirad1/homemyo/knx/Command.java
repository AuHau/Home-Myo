package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Command class which defines XML format of command and also serves as
 * class which maintain commands in application.
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
@Root(name = "emp", strict = false)
public class Command {

    @Attribute
    private int id;

    @Attribute(required = false)
    private boolean display;

    @Element(required = false)
    private String name;

    @Element(required = false)
    private String userName;

    @Element(required = false)
    private String description;

    @Element(name = "graddress",required = false)
    private String address;

    @Element(name = "knxtype",required = false)
    private KnxDataTypeEnum dataType;

    @Element(name = "type", required = false)
    private KnxElementTypes elementType;

    public Command() {
    }

    public Command(int id) {
        this.id = id;
    }

    public Command(int id, String name, String description, String address, KnxDataTypeEnum dataType, KnxElementTypes elementTypes) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.address = address;
        this.dataType = dataType;
        this.elementType = elementTypes;
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

    public String getAddress() {
        return address;
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

    public boolean isDisplay() {
        return display;
    }

    public void setDisplay(boolean display) {
        this.display = display;
    }

    public KnxElementTypes getElementType() {
        return elementType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
