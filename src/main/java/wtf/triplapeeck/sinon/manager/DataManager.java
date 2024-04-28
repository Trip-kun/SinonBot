package wtf.triplapeeck.sinon.manager;

import wtf.triplapeeck.sinon.Config;
import wtf.triplapeeck.sinon.Logger;
import wtf.triplapeeck.sinon.entity.AccessibleEntity;
import wtf.triplapeeck.sinon.entity.ClosableEntity;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public abstract class DataManager<T extends AccessibleEntity> implements Runnable {
    boolean requestToEnd = false;
    protected final ArrayList<String> temp = new ArrayList<>();
    protected final ConcurrentHashMap<String, T> dataCache = new ConcurrentHashMap<>();
    public synchronized void requestToEnd() {
        requestToEnd = true;
    }
    protected abstract T getRawData(String id);
    public abstract void saveData(String id, boolean remove);
    public abstract void removeData(String id);
    protected abstract List<T> getAllRawData();
    public synchronized ClosableEntity<T> getData(String id) {
        T data;
        if (dataCache.get(id)==null) {
            data = getRawData(id);
            dataCache.put(id, data);
        } else {
            data = dataCache.get(id);
        }
        return new ClosableEntity<>(data);
    }
    public synchronized List<ClosableEntity<T>> getAllData() {
        List<T> dataList = getAllRawData();
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

    @Override
    public void run() {
        while (true) {
            if (requestToEnd) {
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
                    removeData(key);
                    dataCache.remove(key);
                }
            }
            temp.clear();
        }
    }
}
