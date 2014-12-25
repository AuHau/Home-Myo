package cz.cvut.uhlirad1.homemyo;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.CommandParserFactory;
import cz.cvut.uhlirad1.homemyo.knx.ICommandParser;
import cz.cvut.uhlirad1.homemyo.localization.IRoomParser;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.localization.RoomParserFactory;
import cz.cvut.uhlirad1.homemyo.service.tree.Combo;
import cz.cvut.uhlirad1.homemyo.service.tree.MyoPose;
import cz.cvut.uhlirad1.homemyo.service.tree.Node;
import cz.cvut.uhlirad1.homemyo.service.tree.TreeParser;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;
import org.androidannotations.annotations.sharedpreferences.Pref;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 24.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
@EBean(scope = EBean.Scope.Singleton)
public class AppData {

    private Map<Integer, Node> rootTree;
    private List<Room> rootRooms;
    private Map<Integer, Command> commands;
    private Map<Integer, Room> rooms;
    private Map<String, Room> roomMapping;

    private IRoomParser roomParser;
    private ICommandParser commandParser;
    private TreeParser treeParser;

    private int highestComboId = 0;

    @Pref
    protected AppPreferences_ preferences;

    @RootContext
    protected Context context;

    @AfterInject
    protected void init(){
        roomParser = RoomParserFactory.createParser();
        commandParser = CommandParserFactory.createCommandParser();

        parseRooms();
        parseCommands();

        File treeConfig = getTreeConfig();
        if (treeConfig != null) {
            treeParser = new TreeParser();
            rootRooms = treeParser.parse(treeConfig);
            rootTree = new HashMap<Integer, Node>();
            transferListToTree();
        }
    }

    private File getRoomsConfig(){
        File configDir = new File(Environment.getExternalStorageDirectory() + File.separator + preferences.applicationFolder().get());

        if(!configDir.exists() && !configDir.mkdirs()){
            // TODO: Error Handling
            Log.e("XMLRoomParser", "Config directory does not exist and can not be created!");
            return null;
        }

        File config = new File (configDir, preferences.roomConfig().get());
        if (!config.exists()) {
            // TODO: Error Handling
            Log.e("XMLRoomParser", "Rooms config can not be found!");
            return null;
        }

        return config;
    }

    private void parseRooms(){
        File config = getRoomsConfig();
        if (config != null){
            rooms = roomParser.parse(config);
            roomMapping = roomParser.parseMapping();
        }
    }

    public void commitRooms(){
        File config = getRoomsConfig();
        if (config != null) roomParser.save(config, rooms);
    }

    private File getCommandsConfig(){
        File configDir = new File(Environment.getExternalStorageDirectory() + File.separator + preferences.applicationFolder().get());

        if (!configDir.exists() && !configDir.mkdirs()) {
            // TODO: Error Handling
            Log.e("Tree", "Config directory does not exist and can not be created!");
            return null;
        }

        File config = new File(configDir, preferences.commandConfig().get());
        if (!config.exists()) {
            // TODO: Error Handling
            Log.e("Tree", "Commands config can not be found!");
            return null;
        }

        return config;
    }

    private void parseCommands(){
        File config = getCommandsConfig();
        if(config != null) commands = commandParser.parse(config);
    }

    public void commitCommands(){
        File config = getCommandsConfig();
        if(config != null) commandParser.save(config, commands);
    }

    private File getTreeConfig() {
        File config = new File(context.getExternalFilesDir(null), preferences.treeConfig().get());

        if (!config.exists()) {
            // TODO: Error Handling
            Log.e("Tree", "Commands config can not be found!");
            return null;
        }

        return config;
    }

    public void commitTree(){
        File config = getTreeConfig();
        if (config != null) {
            treeParser.save(config, rootRooms);
            transferListToTree();
        }
    }

    public Combo getCombo(int id) {
        return getCombo(id, -1);
    }

    // TODO: Tahle metoda nemá uplně smysl
    public Combo getCombo(int id, int roomId) {
        if (roomId >= 0) {
            Room room = findRoom(id, rootRooms);
            for (Combo combo : room.getCombo()) {
                if (combo.getId() == id) return combo;
            }
        } else {
            for (Room room : rootRooms) {
                for (Combo combo : room.getCombo()) {
                    if(combo.getId() == id) return combo;
                }
            }
        }
        return null;
    }

    public void removeCombo(int id) {
        int pos, roomPos = 0, deleteRoom = -1, foundPosition = -1;
        for (Room room : rootRooms) {
            pos = 0;

            for (Combo combo : room.getCombo()) {
                if (combo.getId() == id) {
                    foundPosition = pos;
                    break;
                }
            }

            if(foundPosition >= 0){
                if(room.getCombo().size() == 1){
                    deleteRoom = roomPos;
                }else{
                    room.getCombo().remove(foundPosition);
                }
                break;
            }

            roomPos++;
        }

        if (deleteRoom >= 0) {
            rootRooms.remove(deleteRoom);
        }
    }

    public void addCombo(Combo combo, int roomId) {
        for (Room room : rootRooms) {
            if (room.getId() == roomId) room.getCombo().add(combo);
        }
    }

    public void moveCombo(Combo movedCombo, int fromRoomId, int toRoomId) {
        int pos;
        for (Room room : rootRooms) {
            pos = 0;
            for (Combo combo : room.getCombo()) {
                if (combo.getId() == movedCombo.getId()) {
                    room.getCombo().remove(pos);
                    break;
                }
            }

            if (room.getId() == toRoomId) {
                room.getCombo().add(movedCombo);
            }
        }
    }

    public int getHighestComboIdAndRaise() {
        return ++highestComboId;
    }

    private Room findRoom(int id, Map<Integer, Room> roomMap) {
        Room room;
        for (Map.Entry entryRoom: roomMap.entrySet()) {
            room = (Room) entryRoom.getValue();
            if(id == room.getId()) return  room;
        }
        return null;
    }

    private Room findRoom(int id, List<Room> roomList) {
        for (Room room : roomList) {
            if(id == room.getId()) return  room;
        }
        return null;
    }


    private void transferListToTree(){
        for(Room room : rootRooms){
            processRoom(rootTree, room);
        }
    }

    private void processRoom(Map<Integer, Node> map,  Room room){
        Node root = new Node();
        map.put(room.getId(), root);

        for(Combo combo : room.getCombo()){
            if(combo.getId() > highestComboId) highestComboId = combo.getId();
            processCombo(root, combo.getCommandId(), combo.getMyoPose(), 0);
        }
    }

    private void processCombo(Node tree, int commandId, List<MyoPose> poses, int actualPose){

        // All poses have been already applied, save command
        if(actualPose >= poses.size()){
            tree.setCommand(commands.get(commandId));
            return;
        }

        // If there is already existing Node with pose, follow that path otherwise create new Node
        Pose pose = poses.get(actualPose).getType();
        if(tree.getChild(pose) != null){
            processCombo(tree.getChild(pose), commandId, poses, ++actualPose);
        }else{
            Node node = new Node(pose);
            tree.addChild(pose, node);
            processCombo(node, commandId, poses, ++actualPose);
        }
    }

    public Map<Integer, Node> getRootTree() {
        return rootTree;
    }

    public void setRootTree(Map<Integer, Node> rootTree) {
        this.rootTree = rootTree;
    }

    public List<Room> getRootRooms() {
        return rootRooms;
    }

    public void setRootRooms(List<Room> rootRooms) {
        this.rootRooms = rootRooms;
    }

    public Map<Integer, Command> getCommands() {
        return commands;
    }

    public void setCommands(Map<Integer, Command> commands) {
        this.commands = commands;
    }

    public Map<Integer, Room> getRooms() {
        return rooms;
    }

    public void setRooms(Map<Integer, Room> rooms) {
        this.rooms = rooms;
    }

    public Map<String, Room> getRoomMapping() {
        return roomMapping;
    }
}
