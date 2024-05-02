package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public abstract class GuildData extends AccessibleDataEntity{

    public abstract @NotNull Integer getStarboardThreshold();
    public abstract void setStarboardThreshold(@NotNull Integer starboardThreshold);
    public abstract @Nullable String getStarboardChannelID();
    public abstract void setStarboardChannelID(@Nullable String starboardChannelID);
    public abstract @NotNull Boolean isCurrencyEnabled();
    public abstract void setCurrencyEnabled(@NotNull Boolean currencyEnabled);
    public abstract @NotNull Boolean isTestingEnabled();
    public abstract void setTestingEnabled(@NotNull Boolean testingEnabled);
    public abstract @NotNull Collection<? extends CustomCommandData> getCustomCommands();
    public abstract @NotNull Collection<? extends CustomResponseData> getCustomResponses();
}
