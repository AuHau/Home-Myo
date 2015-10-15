package cz.cvut.uhlirad1.homemyo.knx.cat;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import cz.cvut.uhlirad1.homemyo.knx.IAdapter;
import cz.cvut.uhlirad1.homemyo.knx.ITelegram;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
@EBean
public class CatAdapter extends AsyncTask<ITelegram, Void, Void> implements IAdapter {

    @Pref
    protected AppPreferences_ preferences;

    private static HashMap<Integer, Boolean> states = new HashMap<Integer, Boolean>();;

    private Socket socket;

    @RootContext
    protected Context context;

    public CatAdapter() {

    }

    @Override
    protected Void doInBackground(ITelegram... params) {
        sendCatTelegram(params[0]);
        return null;
    }


    @Override
    public boolean sendTelegram(ITelegram telegram) throws IllegalStateException {
        CatAdapter_.getInstance_(context).execute(telegram);
        return false;
    }

    private void sendCatTelegram(ITelegram telegram) {
        try {
            socket = new Socket(preferences.knxIp().get(), preferences.knxPort().get());

            OutputStream out = socket.getOutputStream();
            PrintWriter output = new PrintWriter(out);

            boolean actualState = (Boolean) getState(telegram);
            telegram.setBoolean(!actualState);

            output.println(telegram.assembleTelegram());
            output.flush();

            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean getCatState(PrintWriter output, ITelegram telegram) throws IOException {
        output.println("[read," + telegram.getCommand().getAddress() + "]");
        output.flush();

        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String request = in.readLine();

        Log.i("CatAdapter", "Recieved state - " + request);
        if (request.equals("true") || request.equals("false")) {
            return Boolean.parseBoolean(request);
        }

        Log.e("CatAdapter", "getState() - respond was not a boolean value");
        return false;
    }

    @Override
    public Object getState(ITelegram telegram) {
        int commandId = telegram.getCommand().getId();
        if(!states.containsKey(commandId)){
            states.put(commandId, false);
            return false;
        }

        states.put(commandId, !states.get(commandId));
        return states.get(commandId);
    }

}
