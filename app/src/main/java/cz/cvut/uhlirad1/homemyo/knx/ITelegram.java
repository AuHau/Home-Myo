package cz.cvut.uhlirad1.homemyo.knx;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 4.12.14
 */
public interface ITelegram {

    public String assembleTelegram();
    public void setCommand(Command command);
    public Command getCommand();
    public void setBoolean(boolean data) throws IllegalStateException;
    public void setFloat(float data) throws IllegalStateException;
    public void setTime(String data) throws IllegalStateException;
    public void setDate(String data) throws IllegalStateException;
}
