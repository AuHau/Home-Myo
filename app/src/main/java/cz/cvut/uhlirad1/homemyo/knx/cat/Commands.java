package cz.cvut.uhlirad1.homemyo.knx.cat;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created on 23.12.14
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
@Root(name = "doc",strict = false)
public class Commands {

    @ElementList(name = "elements")
    private List<Command> commands;

    public List<Command> getCommands() {
        return commands;
    }

    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}
