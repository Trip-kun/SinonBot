package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.CustomCommandData;
import wtf.triplapeeck.sinon.entity.GuildData;

@DatabaseTable(tableName = "oatmeal_custom_commands")
public class ORMLiteCustomCommandData extends CustomCommandData {
    @DatabaseField(generatedId = true)
    private Integer id;
    @DatabaseField(canBeNull=false, width=5000)
    private String commandName;
    @DatabaseField(canBeNull=false, width=5000)
    private String response;
    @DatabaseField(canBeNull=false,foreign = true, foreignAutoRefresh = true)
    public ORMLiteGuildData guild;
    public ORMLiteCustomCommandData() {}
    public ORMLiteCustomCommandData(String ignored) {}
    @Override
    public @NotNull String getCommandName() {
        return commandName;
    }
    @Override
    public @NotNull String getResponse() {
        return response;
    }
    @Override
    public @NotNull ORMLiteGuildData getGuild() {
        return guild;
    }
    @Override
    public void setCommandName(@NotNull String commandName) {
        this.commandName = commandName;
    }
    @Override
    public void setResponse(@NotNull String response) {
        this.response = response;
    }
    @Override
    public void setGuild(@NotNull GuildData guild) {
        this.guild = (ORMLiteGuildData) guild;
    }
    @Override
    public @NotNull String getID() {
        return String.valueOf(id);
    }
    @Override
    public void load() {
    }
}
