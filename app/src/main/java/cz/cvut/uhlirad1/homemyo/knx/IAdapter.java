package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.knx.cat.CatTelegram;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 3.12.14
 */
public interface IAdapter {

    /**
     * Method will send telegram into KNX network.
     * Data can be bind through set* methods of Telegram, if no data
     * will be bind, method will send empty command.
     * If telegram doesn't have bound Command, IllegalStateException will
     * be thrown.
     *
     * @param telegram
     * @throws IllegalStateException
     * @return
     */
    public boolean sendTelegram(ITelegram telegram) throws IllegalStateException;


}
