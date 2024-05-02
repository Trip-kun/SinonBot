package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class PlayerSpotData extends AccessibleDataEntity{
    public abstract @NotNull Integer getBet();
    public abstract void setBet(@NotNull Integer bet);
    public abstract @NotNull Boolean getInsured();
    public abstract void setInsured(@NotNull Boolean insured);
    public abstract @NotNull Boolean getDoubled();
    public abstract void setDoubled(@NotNull Boolean doubled);
    public abstract @NotNull Boolean getDoubled2();
    public abstract void setDoubled2(@NotNull Boolean doubled2);
    public abstract@NotNull  Boolean getSplit();
    public abstract void setSplit(@NotNull Boolean split);
    public abstract @NotNull Collection<? extends PlayerCardData> getCards();
    public abstract ChannelData getChannel();
    public abstract void setChannel(ChannelData channel);
    public abstract @NotNull MemberData getPlayer();
    public abstract void setPlayer(@NotNull MemberData player);

}
