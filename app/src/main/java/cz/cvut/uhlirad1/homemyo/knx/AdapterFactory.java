package cz.cvut.uhlirad1.homemyo.knx;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import cz.cvut.uhlirad1.homemyo.R;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatAdapter;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public final class AdapterFactory{

    private AdapterFactory() {
    }

    public static IAdapter createAdapter(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);

        // CAT intl. rozvaděč
        // return new CatAdapter("192.168.88.252", 3671);

        String ip = preferences.getString("knx_ip", context.getString(R.string.default_knx_ip));
//        int port = preferences.getInt("knx_port", 1111);
        int port = 111;

        return new CatAdapter(ip, port);
    }
}
