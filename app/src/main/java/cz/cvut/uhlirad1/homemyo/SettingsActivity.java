package cz.cvut.uhlirad1.homemyo;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 7.12.14
 */
public class SettingsActivity extends Activity {
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
        }

        public static class PrefsFragment extends PreferenceFragment {

            @Override
            public void onCreate(Bundle savedInstanceState) {
                super.onCreate(savedInstanceState);

                addPreferencesFromResource(R.xml.preferences);
            }
        }
    }