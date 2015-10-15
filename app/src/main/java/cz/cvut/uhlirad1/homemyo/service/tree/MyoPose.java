package cz.cvut.uhlirad1.homemyo.service.tree;

import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.R;
import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public MyoPose(Pose pose) {
        type = pose;
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

    public static MyoPose createMyoPoseFromIconResource(int resource) {
        MyoPose pose = new MyoPose();

        switch (resource) {
            case R.drawable.ic_gest_double_tap:
                pose.setType(Pose.DOUBLE_TAP);
                break;
            case R.drawable.ic_gest_spread:
                pose.setType(Pose.FINGERS_SPREAD);
                break;
            case R.drawable.ic_gest_fist:
                pose.setType(Pose.FIST);
                break;
            case R.drawable.ic_get_in:
                pose.setType(Pose.WAVE_IN);
                break;
            case R.drawable.ic_gest_out:
                pose.setType(Pose.WAVE_OUT);
                break;
        }

        return pose;
    }

    public static List<MyoPose> getArray() {
        List<MyoPose> list = new ArrayList<MyoPose>();

//        list.add(new MyoPose(Pose.DOUBLE_TAP));
        list.add(new MyoPose(Pose.FINGERS_SPREAD));
        list.add(new MyoPose(Pose.FIST));
        list.add(new MyoPose(Pose.WAVE_IN));
        list.add(new MyoPose(Pose.WAVE_OUT));

        return list;
    }
}
