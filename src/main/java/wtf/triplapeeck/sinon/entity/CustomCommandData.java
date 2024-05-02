package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;

public abstract class CustomCommandData extends AccessibleDataEntity{
    public abstract @NotNull String getCommandName();
    public abstract void setCommandName(@NotNull String commandName);
    public abstract @NotNull String getResponse();
    public abstract void setResponse(@NotNull String response);
    public abstract @NotNull GuildData getGuild();
    public abstract void setGuild(@NotNull GuildData guild);
}
