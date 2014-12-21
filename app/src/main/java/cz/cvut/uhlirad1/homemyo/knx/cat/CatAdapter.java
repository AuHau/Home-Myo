package cz.cvut.uhlirad1.homemyo.knx.cat;

import android.os.AsyncTask;
import cz.cvut.uhlirad1.homemyo.knx.IAdapter;
import cz.cvut.uhlirad1.homemyo.knx.ITelegram;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public class CatAdapter extends AsyncTask<CatTelegram, Void, Boolean> implements IAdapter {

    private String serverIpAddress;
    private int serverPortNumber;

    public CatAdapter(String serverIpAddress, int serverPortNumber) {
        this.serverIpAddress = serverIpAddress;
        this.serverPortNumber = serverPortNumber;
    }

    @Override
    public boolean sendTelegram(ITelegram telegram) throws IllegalStateException {
        try {
            Socket socket = new Socket(serverIpAddress, serverPortNumber);

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
    protected Boolean doInBackground(CatTelegram... params) {
        return sendTelegram(params[0]);
    }
}
