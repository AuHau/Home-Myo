package cz.cvut.uhlirad1.homemyo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import cz.cvut.uhlirad1.homemyo.knx.AdapterFactory;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.KnxDataTypeEnum;
import cz.cvut.uhlirad1.homemyo.knx.KnxElementTypes;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatAdapter;
import cz.cvut.uhlirad1.homemyo.knx.cat.CatTelegram;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void sendPacket(View view){
        Command command = new Command(1, "Example", "Example", "1/1/1", KnxDataTypeEnum.BOOLEAN, KnxElementTypes.LIGHTN);

        CatTelegram telegram = new CatTelegram();
        telegram.setCommand(command);
        telegram.setBoolean(true);

        CatAdapter adapter = (CatAdapter) AdapterFactory.createAdapter();

        adapter.execute(telegram);
    }
}
