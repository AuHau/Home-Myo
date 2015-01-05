package cz.cvut.uhlirad1.homemyo.knx;

/**
 * Interface for class which contain attributes to send
 * commands into KNX network.
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
public interface ITelegram {

    /**
     * Method will assemble String message for Adapter command.
     * @return
     */
    public String assembleTelegram();
    public void setCommand(Command command);
    public Command getCommand();

    /**
     * Method which will assign boolean data to telegram.
     * If there was some data already set before, IllegalStateException will be thrown
     *
     * @param data
     * @throws IllegalStateException
     */
    public void setBoolean(boolean data) throws IllegalStateException;

    /**
     * Method which will float boolean data to telegram.
     * If there was some data already set before, IllegalStateException will be thrown
     *
     * @param data
     * @throws IllegalStateException
     */
    public void setFloat(float data) throws IllegalStateException;

    /**
     * Method which will time boolean data to telegram.
     * If there was some data already set before, IllegalStateException will be thrown
     *
     * @param data
     * @throws IllegalStateException
     */
    public void setTime(String data) throws IllegalStateException;

    /**
     * Method which will assign date data to telegram.
     * If there was some data already set before, IllegalStateException will be thrown
     *
     * @param data
     * @throws IllegalStateException
     */
    public void setDate(String data) throws IllegalStateException;
}
