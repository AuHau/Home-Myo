package cz.cvut.uhlirad1.homemyo;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;
import com.thalmic.myo.Hub;
import com.thalmic.myo.scanner.ScanActivity;
import cz.cvut.uhlirad1.homemyo.knx.AdapterFactory;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.KnxDataTypeEnum;
import cz.cvut.uhlirad1.homemyo.knx.KnxElementTypes;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatAdapter;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatTelegram;
import cz.cvut.uhlirad1.homemyo.localization.*;
import cz.cvut.uhlirad1.homemyo.service.ListeningService;
import cz.cvut.uhlirad1.homemyo.service.ListeningService_;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import cz.cvut.uhlirad1.homemyo.settings.SettingsActivity;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public class MainActivity extends Activity {

    @Pref
    protected AppPreferences_ preferences;

    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TODO: Kontrolovat Room Combos, zdali se nepřekrývají s All Combos (ty by se v některých místnosti tedy nemohli zpustit). Což by nemuselo být na škodu, ale uživatel by o tom měl vědět.

        // TODO: Zkontrolovat jestli existují Configy pro Rooms a Commands

        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("LOCK_TIME", 2);
        PreferenceManager.getDefaultSharedPreferences(this).edit().putInt("knx_port", 100);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        serviceIntent = new Intent(this, ListeningService_.class);

        // TODO: Když Service skončí Switch by se měl přepnout do původní polohy (v případě že to nebylo vyvoláno uživatelem)
        Switch serviceSwitch = (Switch) menu.findItem(R.id.action_layout_switch_daemon).getActionView().findViewById(R.id.action_service_switch);
        serviceSwitch.setChecked(isListeningServiceRunning());
        serviceSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    startService(serviceIntent);
                }else{
                    stopService(serviceIntent);
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
    
    private boolean isListeningServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (ListeningService_.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
