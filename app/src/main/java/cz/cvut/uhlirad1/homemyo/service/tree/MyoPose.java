package cz.cvut.uhlirad1.homemyo.service.tree;

import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.R;
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

    public int getIconResource() {
        switch (type) {
            case DOUBLE_TAP:
                return R.drawable.ic_gest_double_tap;
            case FINGERS_SPREAD:
                return R.drawable.ic_gest_spread;
            case FIST:
                return R.drawable.ic_gest_fist;
            case WAVE_IN:
                return R.drawable.ic_get_in;
            case WAVE_OUT:
                return R.drawable.ic_gest_out;
            default:
                return 0;
        }
    }
}
