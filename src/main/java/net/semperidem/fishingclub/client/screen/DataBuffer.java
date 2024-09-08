package net.semperidem.fishingclub.client.screen;

public interface DataBuffer {

    long getTime();

    int getRefreshRate();

    long lastRefreshTime();
    void lastRefreshTime(long time);

    default boolean canRefresh() {
        return lastRefreshTime() - getTime() >= getRefreshRate();
    }

    void refresh();

    default void tick() {
        if (canRefresh()) {
            refresh();
            lastRefreshTime(getTime());
        }
    }

}
