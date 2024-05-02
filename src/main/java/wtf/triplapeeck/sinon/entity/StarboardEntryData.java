package wtf.triplapeeck.sinon.entity;

import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;

@DatabaseTable(tableName = "oatmeal_starboard_entries")
public abstract class StarboardEntryData extends AccessibleDataEntity{
    public abstract @NotNull String getOriginalMessageID();
    public abstract void setOriginalMessageID(@NotNull String originalMessageID);
    public abstract @NotNull String getStarboardMessageID();
    public abstract void setStarboardMessageID(@NotNull String starboardMessageID);
    public abstract @NotNull GuildData getGuild();
    public abstract void setGuild(@NotNull GuildData guild);
}
