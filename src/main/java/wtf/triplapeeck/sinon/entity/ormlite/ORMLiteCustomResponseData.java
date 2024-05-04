package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.CustomResponseData;
import wtf.triplapeeck.sinon.entity.GuildData;

@DatabaseTable(tableName = "oatmeal_custom_responses")
public class ORMLiteCustomResponseData extends CustomResponseData {
    @DatabaseField(generatedId = true)
    private @NotNull Integer id;
    @DatabaseField(canBeNull=false, foreign = true, foreignAutoRefresh = true)
    private @NotNull ORMLiteGuildData guild;
    @DatabaseField(canBeNull=false, width=5000)
    private @NotNull String trigger;
    @DatabaseField(canBeNull=false, width=5000)
    private @NotNull String response;
    public ORMLiteCustomResponseData() {}
    public ORMLiteCustomResponseData(String ignored) {}
    @Override
    public @NotNull String getTrigger() {
        return trigger;
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
    public void setTrigger(@NotNull String trigger) {
        this.trigger = trigger;
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
