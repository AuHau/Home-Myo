package cz.cvut.uhlirad1.homemyo.knx.cat;

import org.androidannotations.annotations.sharedpreferences.SharedPref;

/**
 * Created on 27.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
@SharedPref(value=SharedPref.Scope.UNIQUE)
public interface Location {

    int location();
}
