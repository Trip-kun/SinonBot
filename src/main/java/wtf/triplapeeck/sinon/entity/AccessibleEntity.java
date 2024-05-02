package wtf.triplapeeck.sinon.entity;

import java.time.Instant;

public abstract class AccessibleEntity {
    private transient int accessCount=0;
    private transient long epoch=0L;
    private transient boolean saved=false;
    public synchronized void requestAccess() {
        accessCount++;
        epoch=System.currentTimeMillis();
    }
    public synchronized void releaseAccess() {
        accessCount--;
        if (accessCount==0) {
            epoch= Instant.now().getEpochSecond();
        }
    }
    public synchronized long getEpoch() {
        return epoch;
    }
    public synchronized void resetEpoch() {
        epoch=0L;
    }
    public synchronized boolean isSaved() {
        return saved;
    }
    public synchronized void setSaved(boolean saved) {
        this.saved=saved;
    }
    public synchronized int getAccessCount() {
        return accessCount;
    }
    public abstract void load();
}
