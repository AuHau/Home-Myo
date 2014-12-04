package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.knx.cat.CatAdapter;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public final class AdapterFactory extends android.app.Application{

    private AdapterFactory() {
    }

    public static IAdapter createAdapter(){
//        preferences.getString("serverIp", null);
        // TODO: Načíst ip adresu a port z nastavení
        return new CatAdapter("192.168.1.11", 1112);
    }
}
