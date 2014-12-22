package cz.cvut.uhlirad1.homemyo.knx.cat;

import android.os.AsyncTask;
import cz.cvut.uhlirad1.homemyo.knx.IAdapter;
import cz.cvut.uhlirad1.homemyo.knx.ITelegram;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.HashMap;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
@EBean
public class CatAdapter implements IAdapter {

    @Pref
    protected AppPreferences_ preferences;

    private HashMap<Integer, Boolean> states;

    public CatAdapter() {
        states = new HashMap<Integer, Boolean>();
    }

    @Override
    public boolean sendTelegram(ITelegram telegram) throws IllegalStateException {
        try {
            Socket socket = new Socket(preferences.knxIp().get(), preferences.knxPort().get());

            OutputStream out = socket.getOutputStream();
            PrintWriter output = new PrintWriter(out);

            output.println(telegram.assembleTelegram());
            output.flush(); out.close();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public Object getState(ITelegram telegram) {
        // TODO: Opravdické ověřování stavu z KNX sítě
        int commandId = telegram.getCommand().getId();
        if(!states.containsKey(commandId)){
            states.put(commandId, true);
            return true;
        }

        boolean aBoolean = states.get(commandId);
        states.put(commandId, !aBoolean);
        return states.get(commandId);
    }

}
