package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;

public abstract class CustomResponseData extends AccessibleDataEntity {
    public abstract @NotNull String getTrigger();
    public abstract void setTrigger(@NotNull String trigger);
    public abstract @NotNull String getResponse();
    public abstract void setResponse(@NotNull String response);
    public abstract @NotNull GuildData getGuild();
    public abstract void setGuild(@NotNull GuildData guild);
}
