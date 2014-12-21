package cz.cvut.uhlirad1.homemyo.knx;

import cz.cvut.uhlirad1.homemyo.knx.cat.CatTelegram;

/**
 * Author: Adam Uhlíř <uhlir.a@gmail.com>
 * Date: 20.12.14
 */
public final class TelegramFactory {
    private TelegramFactory() {
    }

    public static ITelegram createTelegram(){
        return new CatTelegram();
    }
}
