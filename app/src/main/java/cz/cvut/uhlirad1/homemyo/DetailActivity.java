package cz.cvut.uhlirad1.homemyo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.service.tree.Combo;
import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import org.androidannotations.annotations.*;


@EActivity
public class DetailActivity extends Activity {

    @Extra
    protected int comboId;

    @Extra
    protected int roomId;

    @Bean
    protected AppData data;

    private Combo combo;

    @ViewById
    protected TextView commandName;

    @ViewById
    protected TextView commandAddress;

    @ViewById
    protected TextView commandType;

    @ViewById
    protected TextView roomName;

//    @ViewById
//    protected ImageView commandTypeIcon;

    @ViewById
    protected TableLayout gesturesContainer;


    private Integer[][] gestures = {
                                        {R.id.gestNumber1, R.id.gestIcon1, R.id.gestName1},
                                        {R.id.gestNumber2, R.id.gestIcon2, R.id.gestName2},
                                        {R.id.gestNumber3, R.id.gestIcon3, R.id.gestName3}
                                    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
    }

    private void init() {
        combo = data.getCombo(comboId);

        if(combo.getName() != null && !combo.getName().isEmpty()) {
            setTitle(combo.getName());
        }

        Command command = data.getCommands().get(combo.getCommandId());
        commandName.setText(command.getName());
        commandAddress.setText(command.getAddress());
        commandType.setText(command.getElementType().toNiceString());

        roomName.setText(data.getRooms().get(roomId).getName());

        // TODO: Zprovoznit zobrazení ikonky u typu elementu
//        commandTypeIcon.setImageResource(spinnerCommand.getElementType().getIconResource());

        int i = 0;
        TextView number, name;
        ImageView icon;
        for (MyoPose pose : combo.getMyoPose()) {
            number = (TextView) findViewById(gestures[i][0]);
            name = (TextView) findViewById(gestures[i][2]);
            icon = (ImageView) findViewById(gestures[i][1]);

            number.setText(Integer.toString(i + 1) + ".");
            name.setText(pose.toNiceString());
            icon.setImageResource(pose.getIconResource());
            i++;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    @OptionsItem
    public void actionDelete() {
        data.removeCombo(comboId);
        data.commitTree();

        MainActivity_.intent(this).start();
    }

    @OptionsItem
    public void actionEdit() {
        AddActivity_.intent(this).comboId(comboId).roomId(roomId).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.actionSettings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
