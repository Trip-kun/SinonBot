package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.util.Collection;

public abstract class MemberData extends AccessibleDataEntity{
    public abstract @NotNull BigInteger getRak();
    public abstract void setRak(@NotNull BigInteger rak);
    public abstract @NotNull Integer getMessageCount();
    public abstract void setMessageCount(@NotNull Integer messageCount);
    public abstract Collection<? extends PlayerSpotData> getSpots();
}
