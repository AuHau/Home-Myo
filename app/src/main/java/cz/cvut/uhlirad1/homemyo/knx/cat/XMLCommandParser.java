package cz.cvut.uhlirad1.homemyo.knx.cat;

import android.os.Environment;
import android.util.Log;
import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.ICommandsParser;
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
@EBean
public class XMLCommandParser implements ICommandsParser {

    @Pref
    AppPreferences_ preferences;

    List<Command> commands;

    /**
     * Initialization method which will parse data from XML config
     * defined in AppPreferences.
     */
    @AfterInject
    public void init() {
        File configDir = new File(Environment.getExternalStorageDirectory() + File.separator + preferences.applicationFolder().get());

        if (!configDir.exists() && !configDir.mkdirs()) {
            // TODO: Error Handling
            Log.e("XMLCommandParser", "Config directory does not exist and can not be created!");
            return;
        }

        File config = new File(configDir, preferences.commandConfig().get());
        if (!config.exists()) {
            // TODO: Error Handling
            Log.e("XMLCommandParser", "Commands config can not be found!");
            return;
        }

        Serializer serializer = new Persister();
        try {
            Commands commandsContainer = serializer.read(Commands.class, config);
            commands = commandsContainer.getCommands();
            checkData();
        } catch (XMLCommandParserException e) {
            // TODO: Error Handling
            Log.e("XMLCommandParser", "Not valid Commands config data!");

        } catch (Exception e) {
            // TODO: Error Handling
            Log.e("XMLCommandParser", "Parsing of Commands was unsuccessful!");
            e.printStackTrace();
        }
    }

    /**
     * Method for checking consistency of parsed data.
     * In case of some error XMLCommandParserException will be raised.
     * 
     * @throws XMLCommandParserException
     */
    private void checkData() throws XMLCommandParserException {
        for (Command command : commands) {
            if(command.getAddress().isEmpty() || command.getName().isEmpty() ||
                    command.getElementType() == null || command.getDataType() == null)
                throw new XMLCommandParserException("Commands config has not valid data! Invalid data at Command with ID - " + command.getId());
        }
    }


    @Override
    public List<Command> parse() {
        return commands;
    }
}
