package cz.cvut.uhlirad1.homemyo.localization.cat;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.SystemClock;
import cz.cvut.uhlirad1.homemyo.localization.*;

import java.util.HashMap;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public class TrackerCAT implements ITracker {

    private HashMap<String, Room> mapping;

    private SharedPreferences trackerAppPreferences;

    private String TRACKER_APP_PACKAGE = "com.example.rtlslocalizationservice";
    private String TRACKER_APP_PREFERENCE_NAME = "sharedRoom";
    private String TRACKER_APP_PREFERENCE_KEY = "room";

    public TrackerCAT(IRoomsParser parser, Context context) {
        mapping = parser.parseMapping();
        try {
            Context trackerAppContext = context.createPackageContext(TRACKER_APP_PACKAGE, 0);
            trackerAppPreferences = trackerAppContext.getSharedPreferences(TRACKER_APP_PREFERENCE_NAME, Context.MODE_PRIVATE);

        } catch (PackageManager.NameNotFoundException e) {
            // TODO: Co s tím, když nenašel TrackerService? Zobrazit Toast a nastavit že Tracking neni zapnutý...
            e.printStackTrace();
        }
    }

    public  TrackerCAT(IRoomsParser parser, Context context, String trackerAppPackage, String trackerAppPrefName, String trackerAppPrefKey){
        this(parser, context);

        TRACKER_APP_PACKAGE = trackerAppPackage;
        TRACKER_APP_PREFERENCE_NAME = trackerAppPrefName;
        TRACKER_APP_PREFERENCE_KEY = trackerAppPrefKey;
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
