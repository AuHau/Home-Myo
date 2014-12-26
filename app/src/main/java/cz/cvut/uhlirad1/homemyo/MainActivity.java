package cz.cvut.uhlirad1.homemyo;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.Switch;
import cz.cvut.uhlirad1.homemyo.adapters.ComboAdapter;
import cz.cvut.uhlirad1.homemyo.service.ListeningService_;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import cz.cvut.uhlirad1.homemyo.settings.SettingsActivity;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.sharedpreferences.Pref;

@EActivity
public class MainActivity extends ListActivity {

    @Pref
    protected AppPreferences_ preferences;

    @Bean
    protected ComboAdapter adapter;

    Intent serviceIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // TODO: Zkontrolovat jestli existují Configy pro Rooms a Commands

        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        ComboAdapter.Item item = (ComboAdapter.Item) getListAdapter().getItem(position);
        DetailActivity_.intent(this).comboId(item.getComboId()).roomId(item.getRoomId()).start();
    }

    @Override
    protected void onResume() {
        ((ComboAdapter) getListAdapter()).notifyDataSetChanged();
        super.onResume();
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

    @OptionsItem
    public void actionSettings() {
        Intent intent = new Intent();
        intent.setClass(this, SettingsActivity.class);
        startActivity(intent);
    }

    @OptionsItem
    public void actionAdd() {
        AddActivity_.intent(this).start();
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
