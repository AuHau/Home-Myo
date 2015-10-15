package cz.cvut.uhlirad1.homemyo.knx;

/**
 * Interface which specifies Adapter module
 *
 * @author: Adam Uhlíř <uhlir.a@gmail.com>
 */
public interface IAdapter {

    /**
     * Method will send telegram into KNX network.
     * Data can be bind through set* methods of Telegram, if no data
     * will be bind, method will send empty spinnerCommand.
     * If telegram doesn't have bound Command, IllegalStateException will
     * be thrown.
     *
     * @param telegram
     * @throws IllegalStateException
     * @return
     */
    public boolean sendTelegram(ITelegram telegram) throws IllegalStateException;

    /**
     * Method will require state of element specified by Command in telegram.
     * Since state can be expressed with many types (depends on type of element),
     * return value is generally Object and it is up to developer to type cast
     * to proper type.
     *
     * @param telegram
     * @return
     */
    public Object getState(ITelegram telegram);

}
