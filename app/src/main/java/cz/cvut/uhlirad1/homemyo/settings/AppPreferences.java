package cz.cvut.uhlirad1.homemyo.settings;

import cz.cvut.uhlirad1.homemyo.R;
import org.androidannotations.annotations.sharedpreferences.*;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 22.12.14
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface AppPreferences {

    @DefaultFloat(0.0f)
    float appVersion();

    @DefaultString("Home Myo")
    String applicationName();

    @DefaultString("HomeMyo")
    String applicationFolder();

    @DefaultString("rooms.xml")
    String roomConfig();

    @DefaultString("commands.xml")
    String commandConfig();

    // KNX Settings
    @DefaultString("192.168.1.11")
    String knxIp();

    @DefaultInt(1111)
    int knxPort();

    // Localization settings
    @DefaultBoolean(false)
    boolean locEnabled();

    @DefaultInt(1000)
    int trackerWaitingTime();


    // Myo settings
    @DefaultInt(3)
    int lockTimeout();

    String myoMac();

}
