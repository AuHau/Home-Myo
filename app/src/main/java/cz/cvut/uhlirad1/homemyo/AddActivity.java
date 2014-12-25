package cz.cvut.uhlirad1.homemyo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.service.tree.Combo;
import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@EActivity
public class AddActivity extends Activity {

    @Bean
    protected AppData data;

    @ViewById
    protected EditText name;

    @ViewById
    protected Spinner spinnerCommand;

    @ViewById
    protected Spinner spinnerRoom;

    @ViewById
    protected Spinner spinnerGesture1;

    @ViewById
    protected Spinner spinnerGesture2;

    @ViewById
    protected Spinner spinnerGesture3;

    private ArrayList<Command> commandList;
    private ArrayList<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        ArrayAdapter adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, convertCommandsToSpinner());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCommand.setAdapter(adapter);

        ArrayAdapter roomAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, convertRoomsToSpinner());
        roomAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRoom.setAdapter(roomAdapter);

        List list = MyoPose.getArray();
        list.add(0, new MyoPose());
        GestureAdapter gestureAdapter = new GestureAdapter(this, R.layout.gesture, list);
        gestureAdapter.setDropDownViewResource(R.layout.gesture);

        spinnerGesture1.setAdapter(gestureAdapter);
        spinnerGesture2.setAdapter(gestureAdapter);
        spinnerGesture3.setAdapter(gestureAdapter);
    }

    @Click
    public void btnSave() {

        List<MyoPose> poses = new ArrayList<MyoPose>();

        if (((MyoPose)spinnerGesture1.getSelectedItem()).getType() != null) {
            poses.add((MyoPose) spinnerGesture1.getSelectedItem());
        }
        if (((MyoPose)spinnerGesture2.getSelectedItem()).getType() != null) {
            poses.add((MyoPose) spinnerGesture2.getSelectedItem());
        }
        if (((MyoPose)spinnerGesture3.getSelectedItem()).getType() != null) {
            poses.add((MyoPose) spinnerGesture3.getSelectedItem());
        }


        Combo combo = new Combo(
                data.getHighestComboIdAndRaise(),
                commandList.get(spinnerCommand.getSelectedItemPosition()).getId(),
                name.getText().toString(),
                poses
                );


        data.addCombo(combo, roomList.get(spinnerRoom.getSelectedItemPosition()).getId());
        data.commitTree();

        MainActivity_.intent(this).start();
    }

    private List<String> convertCommandsToSpinner(){
        ArrayList<String> list = new ArrayList<String>(data.getCommands().size());
        commandList = new ArrayList<Command>();

        for (Map.Entry<Integer, Command> entry : data.getCommands().entrySet()) {
            list.add(entry.getValue().getName());
            commandList.add(entry.getValue());
        }

        return list;
    }

    private List<String> convertRoomsToSpinner(){
        ArrayList<String> list = new ArrayList<String>(data.getRooms().size());
        roomList = new ArrayList<Room>();

        for (Map.Entry<Integer, Room> entry : data.getRooms().entrySet()) {
            list.add(entry.getValue().getName());
            roomList.add(entry.getValue());
        }

        return list;
    }
}
