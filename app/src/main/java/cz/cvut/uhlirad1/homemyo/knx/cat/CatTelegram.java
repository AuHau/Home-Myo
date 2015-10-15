package cz.cvut.uhlirad1.homemyo.knx.cat;

import cz.cvut.uhlirad1.homemyo.knx.Command;
import cz.cvut.uhlirad1.homemyo.knx.ITelegram;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public class CatTelegram implements ITelegram {

    private boolean booleanData;
    private float floatData;
    private String timeData, dateDate;
    private Command command;

    private boolean dataSet;

    public CatTelegram() {
        dataSet = false;
    }

    public String assembleTelegram(){
        StringBuilder builder = new StringBuilder();
        builder.append("[")
                .append(command.getAddress())
                .append(",")
                .append(getData())
                .append("]");

        return builder.toString();
    }

    private String getData(){
        switch (command.getDataType()){
            case BOOL:
                return String.valueOf(booleanData);

            case DATE:
                return dateDate;

            case FLOAT:
                return String.valueOf(floatData);

            case TIME:
                return timeData;

            default:
                return null;
        }
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    @Override
    public Command getCommand() {
        return command;
    }

    public void setBoolean(boolean data) throws IllegalStateException {
        if(dataSet) throw new IllegalStateException("Data were already set");

        booleanData = data;

        dataSet = true;
    }

    public void setFloat(float data) throws IllegalStateException {
        if(dataSet) throw new IllegalStateException("Data were already set");

        floatData = data;

        dataSet = true;
    }

    public void setTime(String data) throws IllegalStateException {
        if(dataSet) throw new IllegalStateException("Data were already set");

        timeData = data;

        dataSet = true;
    }

    public void setDate(String data) throws IllegalStateException {
        if(dataSet) throw new IllegalStateException("Data were already set");

        dateDate = data;

        dataSet = true;
    }
}
