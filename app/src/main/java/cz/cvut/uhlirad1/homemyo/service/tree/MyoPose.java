package cz.cvut.uhlirad1.homemyo.service.tree;

import com.thalmic.myo.Pose;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 20.12.14
 */
@Root
public class MyoPose {
    @Attribute
    Pose type;

    public MyoPose() {
    }

    public Pose getType() {
        return type;
    }

    public void setType(Pose type) {
        this.type = type;
    }
}
