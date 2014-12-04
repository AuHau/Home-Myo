package cz.cvut.uhlirad1.homemyo.localization;

import java.io.Serializable;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 1.12.14
 */
public final class Room implements Serializable{
    private final int id;
    private final String name;
    private final int order;

    public Room(int id, String name) {
        this.id = id;
        this.name = name;
        order = id;
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
}
