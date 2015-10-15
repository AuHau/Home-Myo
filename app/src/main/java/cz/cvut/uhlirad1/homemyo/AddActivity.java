package cz.cvut.uhlirad1.homemyo;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import cz.cvut.uhlirad1.homemyo.adapters.CommandsAdapter;
import cz.cvut.uhlirad1.homemyo.adapters.GestureAdapter;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.service.tree.Combo;
import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import org.androidannotations.annotations.*;
import org.w3c.dom.Text;

import java.util.*;


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

    @Extra
    protected int comboId;

    @Extra
    protected int roomId;

    private ArrayList<Command> commandList;
    private ArrayList<Room> roomList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        commandList = new ArrayList<Command>(data.getCommands().values());
        Collections.sort(commandList, new CommandComparator());

        CommandsAdapter commandsAdapter = new CommandsAdapter(this, R.layout.command, commandList);
        commandsAdapter.setDropDownViewResource(R.layout.command);
        spinnerCommand.setAdapter(commandsAdapter);

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

        if (comboId > 0) {
            Combo combo = data.getCombo(comboId);
            setTitle((combo.getName() == null ? "Edit" : combo.getName() + " - Edit"));

            name.setText(combo.getName());
            spinnerCommand.setSelection(commandsAdapter.getPosition(data.getCommands().get(combo.getCommandId())));
            spinnerRoom.setSelection(roomAdapter.getPosition(data.getRooms().get(roomId).getName()));

            Spinner[] spinners = {spinnerGesture1, spinnerGesture2, spinnerGesture3};
            int i = 0;
            for (MyoPose pose : combo.getMyoPose()) {
                spinners[i++].setSelection(gestureAdapter.getPosition(findPose(pose, list)));
            }
        }
    }

    private MyoPose findPose(MyoPose searchedPose, List<MyoPose> poses) {
        for (MyoPose pose : poses) {
            if(pose.getType() == searchedPose.getType()) return pose;
        }
        return null;
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

        Combo combo;
        if (comboId > 0) {
            combo = data.getCombo(comboId);
            combo.setCommandId(commandList.get(spinnerCommand.getSelectedItemPosition()).getId());
            combo.setMyoPose(poses);
            combo.setName(name.getText().toString());

            int formRoomId = roomList.get(spinnerRoom.getSelectedItemPosition()).getId();
            if (roomId != formRoomId) {
                data.moveCombo(combo, formRoomId);
            }
        } else {
            combo = new Combo(
                    data.getHighestComboIdAndRaise(),
                    commandList.get(spinnerCommand.getSelectedItemPosition()).getId(),
                    name.getText().toString(),
                    poses
            );
            data.addCombo(combo, roomList.get(spinnerRoom.getSelectedItemPosition()).getId());
        }


        data.commitTree();

        finish();
//        DetailActivity_.intent(this).roomId(roomId).comboId(comboId).start();
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

    private class CommandComparator implements Comparator<Command>{

        @Override
        public int compare(Command lhs, Command rhs) {
            return lhs.getAddress().compareTo(rhs.getAddress());
        }
    }
}
