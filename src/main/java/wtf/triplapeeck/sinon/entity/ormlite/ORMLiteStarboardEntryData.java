package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.GuildData;
import wtf.triplapeeck.sinon.entity.StarboardEntryData;
@DatabaseTable(tableName = "oatmeal_starboard_entries")
public class ORMLiteStarboardEntryData extends StarboardEntryData {
    @DatabaseField(id = true, canBeNull = false)
    private @NotNull String originalMessageID;
    @DatabaseField(canBeNull=false)
    private @NotNull String starboardMessageID;
    @DatabaseField(canBeNull=false, foreign = true, foreignAutoRefresh = true)
    private @NotNull ORMLiteGuildData guild;
    public ORMLiteStarboardEntryData() {
    }
    public ORMLiteStarboardEntryData(@NotNull String ignored) {
        originalMessageID = ignored;
        starboardMessageID="";
    }
    @Override
    public @NotNull String getOriginalMessageID() {
        return originalMessageID;
    }
    @Override
    public @NotNull String getStarboardMessageID() {
        return starboardMessageID;
    }
    @Override
    public @NotNull ORMLiteGuildData getGuild() {
        return guild;
    }
    @Override
    public void setOriginalMessageID(@NotNull String originalMessageID) {
        this.originalMessageID = originalMessageID;
    }
    @Override
    public void setStarboardMessageID(@NotNull String starboardMessageID) {
        this.starboardMessageID = starboardMessageID;
    }
    @Override
    public void setGuild(@NotNull GuildData guild) {
        this.guild = (ORMLiteGuildData) guild;
    }
    @Override
    public @NotNull String getID() {
        return originalMessageID;
    }
    @Override
    public void load() {
    }
}
