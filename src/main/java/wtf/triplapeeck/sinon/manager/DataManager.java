package wtf.triplapeeck.sinon.manager;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.entity.*;

import javax.xml.crypto.Data;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class DataManager<T extends AccessibleDataEntity> implements Runnable {
    public static DataManager<? extends ChannelData> channelDataManager;
    public static DataManager<? extends ChannelMemberData> channelMemberDataManager;
    public static DataManager<? extends CustomCommandData> customCommandDataManager;
    public static DataManager<? extends CustomResponseData> customResponseDataManager;
    public static DataManager<? extends DeckCardData> deckCardDataManager;
    public static DataManager<? extends GuildData> guildDataManager;
    public static DataManager<? extends MemberData> memberDataManager;
    public static DataManager<? extends PlayerCardData> playerCardDataManager;
    public static DataManager<? extends PlayerSpotData> playerSpotDataManager;
    public static DataManager<? extends ReminderData> reminderDataManager;
    public static DataManager<? extends StarboardEntryData> starboardEntryDataManager;
    public static DataManager<? extends UserData> userDataManager;
    private AtomicBoolean requestToEnd = new AtomicBoolean(false);
    protected final ArrayList<String> temp = new ArrayList<>();
    protected final ConcurrentHashMap<String, T> dataCache = new ConcurrentHashMap<>();
    public void requestToEnd() {
        requestToEnd.set(true);
    }
    protected abstract T getRawData(String id);
    public abstract void saveData(String id, boolean remove);
    public abstract void removeData(String id);
    public abstract void removeData(Collection<String> ids);
    protected abstract List<T> getAllRawData();
    protected abstract List<T> queryAllRawData(String query, AccessibleEntity value);
    protected abstract List<T> queryLessThanRawData(String query, Integer value);
    public synchronized ClosableEntity<T> getData(String id) {
        dataCache.putIfAbsent(id, getRawData(id));
        return new ClosableEntity<>(dataCache.get(id));
    }
    /*
        * This method is used to get data that is not cached.
        * This is useful for generating data that doesn't need to be saved, such as temp object to construct a response.
        * This method should be used sparingly, as it generates a new object every time it is called.
        * @param id The ID of the data to get
        * @return The data object
     */
    public synchronized T getUncachedData(String id) {
        return getRawData(id);
    }
    public synchronized List<ClosableEntity<T>> getAllData() {
        List<T> dataList = getAllRawData();
        List<ClosableEntity<T>> data = new ArrayList<>();
        for (T d : dataList) {
            data.add(new ClosableEntity<>(d));
        }
        return data;
    }
    public synchronized List<ClosableEntity<T>> queryAllData(String query, T value) {
        List<T> dataList = queryAllRawData(query, value);
        List<ClosableEntity<T>> data = new ArrayList<>();
        for (T d : dataList) {
            data.add(new ClosableEntity<>(d));
        }
        return data;
    }
    public synchronized void saveAllData(boolean remove) {
        if (remove) {
            temp.addAll(dataCache.keySet());
            for (String key : temp) {
                saveData(key, true);
            }
            temp.clear();
        } else {
            for (String key : dataCache.keySet()) {
                saveData(key, false);
            }
        }
    }
    public synchronized List<ClosableEntity<T>> queryLessThan(String query, Integer value) {
        List<T> dataList = queryLessThanRawData(query, value);
        ArrayList<ClosableEntity<T>> dataOut = new ArrayList<>();
        for (T data : dataList) {
            dataOut.add(new ClosableEntity<>(data));
            dataCache.put(data.getID(), data);
        }
        return dataOut;
    }

    @Override
    public void run() {
        while (true) {
            if (requestToEnd.get()) {
                while (ThreadManager.getInstance().getState()!= Thread.State.TERMINATED) {
                    try {
                        Thread.sleep(Config.getConfig().threadSleep);
                    } catch (InterruptedException e) {
                        Logger.log(Logger.Level.FATAL, e.getMessage());
                        throw new RuntimeException(e);
                    }
                }
                break;
            }
            try {
                Thread.sleep(Config.getConfig().dataThreadSleep);
            } catch (InterruptedException e) { // If the thread is interrupted, which it NEVER should be
                Logger.log(Logger.Level.FATAL, e.getMessage()); // Log the error
                throw new RuntimeException(e); // Throw a runtime exception, as the bot cannot function without the data manager
            }
            for (String key : dataCache.keySet()) {
                T data = dataCache.get(key);
                if (data.getEpoch()!=0 && data.getAccessCount()==0) {
                    if (!data.isSaved()) {
                        saveData(key, false);
                        data.setSaved(true);
                    }
                    if (Instant.now().getEpochSecond() > Config.getConfig().dataLifetime + data.getEpoch()) {
                        temp.add(key);
                    }
                } else {
                    data.resetEpoch();
                    data.setSaved(false);
                }
            }
            for (String key : temp) {
                T data = dataCache.get(key);
                if (data!=null && data.getAccessCount()==0) {
                    dataCache.remove(key);
                }
            }
            temp.clear();
        }
    }
}
