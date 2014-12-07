package cz.cvut.uhlirad1.homemyo.knx;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public class Command {

    private int id;
    private String name, description, address;
    private KnxDataTypeEnum dataType;
    private KnxElementTypes elementType;

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

    public KnxElementTypes getElementsType() {
        return elementType;
    }

    public String getAddress() {
        return address;
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
