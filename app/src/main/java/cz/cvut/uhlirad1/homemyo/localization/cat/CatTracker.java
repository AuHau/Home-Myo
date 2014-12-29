package cz.cvut.uhlirad1.homemyo.localization.cat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import cz.cvut.uhlirad1.homemyo.AppData;
import cz.cvut.uhlirad1.homemyo.knx.cat.Location_;
import cz.cvut.uhlirad1.homemyo.localization.*;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
@EBean
public class CatTracker implements ITracker {

    @Pref
    protected AppPreferences_ preferences;

    @Bean
    protected AppData data;

    @RootContext
    protected Context context;

    @Pref
    protected Location_ location;

    private Map<String, Room> mapping;

    @AfterInject
    public void init() {

        // Reset location module
        location.location().put(-1);

        mapping = data.getRoomMapping();
    }

    @Override
    public Room getLocation() throws TrackerException {
        return getLocation(true);
    }

    @Override
    public Room getLocation(boolean isWaiting) throws TrackerException {

        Room room = mapping.get(location.location().get());
        if(room == null) throw new TrackerException();

        return room;
    }
}
