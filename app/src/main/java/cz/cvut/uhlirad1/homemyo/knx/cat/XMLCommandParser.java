package cz.cvut.uhlirad1.homemyo.knx.cat;

import android.os.Environment;
import android.util.Log;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.ICommandParser;
import cz.cvut.uhlirad1.homemyo.localization.Room;
import cz.cvut.uhlirad1.homemyo.settings.AppPreferences_;
import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.sharedpreferences.Pref;
import org.simpleframework.xml.Serializer;
import org.simpleframework.xml.core.Persister;

import java.io.File;
import java.util.*;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 23.12.14
 */
public class XMLCommandParser implements ICommandParser {

    private Map<Integer, Command> checkData(List<Command> commands) throws XMLCommandParserException {
        HashMap<Integer, Command> map = new HashMap<Integer, Command>();

        for (Command command : commands) {
            if (command.getAddress().isEmpty() || command.getName().isEmpty() ||
                    command.getElementType() == null || command.getDataType() == null) {
                Log.e("XMLCommandParser", "Commands config has not valid data! Invalid data at Command with ID - " + command.getId());
                throw new XMLCommandParserException("Commands configuration file has not valid data!");
            }

            map.put(command.getId(), command);
        }

        return map;
    }


    @Override
    public Map<Integer, Command> parse(File config) throws Exception {
        if (!config.exists()) {
            Log.e("XMLCommandParser", "Commands config doesn't exist!");
            throw new XMLCommandParserException("Commands configuration file can not be found!");
        }

        Serializer serializer = new Persister();
        Commands commandsContainer = serializer.read(Commands.class, config);
        List<Command> commandList = commandsContainer.getCommands();
        return checkData(commandList);
    }

    @Override
    public void save(File config, Map<Integer, Command> commands) throws Exception {
        if (!config.exists()) {
            Log.e("XMLCommandParser", "Commands config doesn't exist!");
            throw new XMLCommandParserException("Commands configuration file can not be found!");
        }

        Serializer serializer = new Persister();
        Commands commandsCommit = new Commands();
        commandsCommit.setCommands(covertToList(commands));
        serializer.write(commandsCommit, config);
    }


    private List<Command> covertToList(Map<Integer, Command> commands) {
        LinkedList<Command> list = new LinkedList<Command>();

        for (Map.Entry entryRoom: commands.entrySet()) {
            list.add((Command) entryRoom.getValue());
        }

        return list;
    }
}
