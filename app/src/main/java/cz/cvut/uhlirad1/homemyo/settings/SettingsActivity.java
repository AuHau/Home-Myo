package cz.cvut.uhlirad1.homemyo.settings;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.widget.Toast;
import com.thalmic.myo.Hub;
import com.thalmic.myo.Myo;
import cz.cvut.uhlirad1.homemyo.R;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.util.List;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 7.12.14
 */
@EActivity
public class SettingsActivity extends Activity {

    @Pref
    protected AppPreferences_ preferences;

    public static String PREF_NAME = "AppPreferences";

    private boolean areMyosConnected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Display the fragment as the main content.
        FragmentManager mFragmentManager = getFragmentManager();
        FragmentTransaction mFragmentTransaction = mFragmentManager
                .beginTransaction();
        PrefsFragment mPrefsFragment = new PrefsFragment();
        mFragmentTransaction.replace(android.R.id.content, mPrefsFragment);
        mFragmentTransaction.commit();

        // Hub has to be initialized so ScanActivity would work
        Hub hub = Hub.getInstance();
        if (hub.getConnectedDevices().size() > 0) areMyosConnected = true;
        if (!areMyosConnected && !hub.init(this, getPackageName())) {
            Toast.makeText(this, "Couldn't initialize Hub", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // TODO: Nasty hack for saving connected Myo MAC address - find better way
        if (!preferences.myoMac().get().isEmpty() && !areMyosConnected) {
            hub.attachByMacAddress(preferences.myoMac().get());
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        List<Myo> myos = Hub.getInstance().getConnectedDevices();
        if (myos.size() > 0) {
            preferences.myoMac().put(myos.get(0).getMacAddress());
        }else{
            preferences.myoMac().put("");
        }

        if(!areMyosConnected) Hub.getInstance().shutdown();
    }

    public static class PrefsFragment extends PreferenceFragment {

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            this.getPreferenceManager().setSharedPreferencesName(PREF_NAME);
            addPreferencesFromResource(R.xml.preferences);
        }
    }
}