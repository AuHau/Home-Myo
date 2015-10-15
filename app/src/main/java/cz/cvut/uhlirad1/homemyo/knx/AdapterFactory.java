package cz.cvut.uhlirad1.homemyo.knx;

import android.content.Context;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatAdapter_;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public final class AdapterFactory{

    private AdapterFactory() {
    }

    public static IAdapter createAdapter(Context context){
        // CAT intl. rozvaděč
//        return new CatAdapter("192.168.88.252", 3671);

        return CatAdapter_.getInstance_(context);
    }
}
