package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.R;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public enum KnxElementTypes {
    CENTRAL_FUNCTION,
    LIGHT,
    BLINDS,
    MEASURED_VALUES,
    TEMPERATURE,
    ALARM_SYSTEM,
    SOCKET;

    public static int getIconResource(KnxElementTypes type) {
        switch (type) {
            case CENTRAL_FUNCTION:
                return R.drawable.ic_center;
            case LIGHT:
                return R.drawable.ic_light;
            case ALARM_SYSTEM:
                return R.drawable.ic_alarm;
            case MEASURED_VALUES:
                return R.drawable.ic_measure;
            case SOCKET:
                return R.drawable.ic_socket;
            case TEMPERATURE:
                return R.drawable.ic_temp;
            default:
                return 0;
        }
    }
}
