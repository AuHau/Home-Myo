package cz.cvut.uhlirad1.homemyo.service.tree;

import com.thalmic.myo.Myo;
import com.thalmic.myo.Pose;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.service.tree.Node;
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

        try {
            Rooms rooms = serializer.read(Rooms.class, config);
            return mapRooms(rooms);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
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

        for(Command command : room.getCommand()){
            processCommand(root, command.getId(), command.getMyoPose());
        }
    }

    private void processCommand(Node tree, int commandId, List<MyoPose> poses){

        // All poses have been already applied, save command
        if(poses.size() == 0){
            tree.setCommand(findCommand(commandId));
            return;
        }

        // If there is already existing Node with pose, follow that path otherwise create new Node
        Pose pose = poses.remove(0).getType();
        if(tree.getChild(pose) != null){
            processCommand(tree.getChild(pose), commandId, poses);
        }else{
            Node node = new Node(pose);
            tree.addChild(pose, node);
            processCommand(node, commandId, poses);
        }
    }

    private Command findCommand(int commandId){
        for(Command command : commands){
            if(command.getId() == commandId) return command;
        }

        return null;
    }
}
