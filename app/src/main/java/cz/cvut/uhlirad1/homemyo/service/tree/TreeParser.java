package cz.cvut.uhlirad1.homemyo.service.tree;

import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 19.12.14
 */
public class TreeParser {

    private List<Command> commands;

    public TreeParser(List<Command> commands) {
        this.commands = commands;
    }

    public Map<Integer, Node> parse(File config){
        Serializer serializer = new Persister();

        Rooms rooms = null;
        try {
            rooms = serializer.read(Rooms.class, config);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Map<Integer,Node> map = mapRooms(rooms);
        return map;
    }

    private Map<Integer, Node> mapRooms(Rooms rooms){
        HashMap<Integer, Node> map = new HashMap<Integer, Node>();

        for(Room room : rooms.getRoom()){
            processRoom(map, room);
        }

        return map;
    }

    private void processRoom(Map<Integer, Node> map,  Room room){
        Node root = new Node();
        map.put(room.getId(), root);

        for(Combo combo : room.getCombo()){
            processCombo(root, combo.getCommandId(), combo.getMyoPose());
        }
    }

    private void processCombo(Node tree, int commandId, List<MyoPose> poses){

        // All poses have been already applied, save command
        if(poses.size() == 0){
            tree.setCommand(findCommand(commandId));
            return;
        }

        // If there is already existing Node with pose, follow that path otherwise create new Node
        Pose pose = poses.remove(0).getType();
        if(tree.getChild(pose) != null){
            processCombo(tree.getChild(pose), commandId, poses);
        }else{
            Node node = new Node(pose);
            tree.addChild(pose, node);
            processCombo(node, commandId, poses);
        }
    }

    private Command findCommand(int commandId){
        for(Command command : commands){
            if(command.getId() == commandId) return command;
        }

        return null;
    }
}
