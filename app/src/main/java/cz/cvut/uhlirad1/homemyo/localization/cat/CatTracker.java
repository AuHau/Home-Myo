package cz.cvut.uhlirad1.homemyo.localization.cat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import cz.cvut.uhlirad1.homemyo.localization.*;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.HashMap;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
@EBean
public class CatTracker implements ITracker {

    @Pref
    protected AppPreferences_ preferences;

    private HashMap<String, Room> mapping;

    private SharedPreferences trackerAppPreferences;

    private String TRACKER_APP_PACKAGE = "com.example.rtlslocalizationservice";
    private String TRACKER_APP_PREFERENCE_NAME = "sharedRoom";
    private String TRACKER_APP_PREFERENCE_KEY = "room";

    public CatTracker(Context context) {
        IRoomsParser parser = RoomsParserFactory.createParser();
        mapping = parser.parseMapping();
        try {
            Context trackerAppContext = context.createPackageContext(TRACKER_APP_PACKAGE, 0);
            trackerAppPreferences = trackerAppContext.getSharedPreferences(TRACKER_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        } catch (PackageManager.NameNotFoundException e) {
            preferences.locEnabled().put(false);
            e.printStackTrace();
        }
    }

    @Override
    public Room getLocation() throws TrackerException {
        return getLocation(true);
    }

    @Override
    public Room getLocation(boolean isWaiting) throws TrackerException {
        String roomString = trackerAppPreferences.getString(TRACKER_APP_PREFERENCE_KEY, null);

        if(roomString == null){
            if(isWaiting) {
                int iteration = 0;

                // TODO: Přijít na lepší řešení než bruteforce sleep
                while (iteration < TrackerFactory.WAITING_ITERATION && roomString == null) {
                    SystemClock.sleep(TrackerFactory.WAITING_TIME);
                    roomString = trackerAppPreferences.getString(TRACKER_APP_PREFERENCE_KEY, null);
                }

                if (roomString == null) throw new TrackerException();
            } else {
                throw new TrackerException();
            }
        }

        Room room = mapping.get(roomString);
        if(room == null) throw new TrackerException();

        return room;
    }
}
