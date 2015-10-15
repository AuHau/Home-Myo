package cz.cvut.uhlirad1.homemyo.localization;

/**
 * Created by adam on 3.12.14.
 */
public interface ITracker {

    /**
     * Method for getting current location. If no location is available
     * default behavior is to wait predefined time in TrackerFactory.WAITING_TIME
     * and repeat this waiting loop until it found location or exceed limit defined
     * in TrackerFactory.WAITING_ITERATION. If even then no location is found
     * TrackerException is thrown
     *
     * @return
     * @throws TrackerException
     */
    public Room getLocation() throws TrackerException;

    /**
     * Similar behavior as getLocation(), except it can be specified if method
     * should or should not wait for location, in case it is not found.
     *
     * @param isWaiting
     * @return
     * @throws TrackerException
     */
    public Room getLocation(boolean isWaiting) throws TrackerException;


}
