package cz.cvut.uhlirad1.homemyo.service.tree;

import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.R;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 20.12.14
 */
@Root(name = "myo-pose")
public class MyoPose {
    @Attribute
    Pose type;

    public MyoPose() {
    }

    public String toNiceString() {
        switch (type) {
            case WAVE_IN:
                return "Wave in";
            case WAVE_OUT:
                return "Wave out";
            case FIST:
                return "Fist";
            case FINGERS_SPREAD:
                return "Fingers spread";
            case DOUBLE_TAP:
                return "Double tap";
            case REST:
                return "Rest";
            case UNKNOWN:
                return "Unknown";
            default:
                return "";
        }
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
