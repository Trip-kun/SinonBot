package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import wtf.triplapeeck.sinon.entity.CustomResponseData;
import wtf.triplapeeck.sinon.entity.GuildData;

import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = "oatmeal_guilds")
public class ORMLiteGuildData extends GuildData {
    @DatabaseField(id = true)
    private @NotNull String id;
    @DatabaseField(canBeNull = true)
    private @Nullable String starboardChannelID;
    @DatabaseField(canBeNull = false)
    private @NotNull Integer starboardThreshold;
    @DatabaseField(canBeNull = false)
    private @NotNull Boolean currencyEnabled;
    @DatabaseField(canBeNull = false)
    private @NotNull Boolean testingEnabled;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLiteCustomResponseData> customResponses;
    @Override
    public void load() {

    }
    public ORMLiteGuildData(String id) {
        this.id = id;
        this.starboardThreshold = 2;
        this.currencyEnabled = true;
        this.testingEnabled = false;
        customResponses = new ArrayList<>();
    }
    public ORMLiteGuildData() {} // For ORMLite

    @Override
    public @NotNull String getID() {
        return id;
    }
    @Override
    public @NotNull Integer getStarboardThreshold() {
        return starboardThreshold;
    }
    @Override
    public void setStarboardThreshold(@NotNull Integer starboardThreshold) {
        this.starboardThreshold = starboardThreshold;
    }
    @Override
    public @Nullable String getStarboardChannelID() {
        return starboardChannelID;
    }
    @NotNull
    @Override
    public Collection<? extends CustomResponseData> getCustomResponses() {
        return customResponses;
    }
    @Override
    public void addCustomResponse(CustomResponseData data) {
        this.customResponses.add((ORMLiteCustomResponseData) data);
    }
    @Override
    public void removeCustomResponse(CustomResponseData data) {
        this.customResponses.remove((ORMLiteCustomResponseData) data);
    }
    @Override
    public void setStarboardChannelID(@Nullable String starboardChannelID) {
        this.starboardChannelID = starboardChannelID;
    }
    @Override
    public @NotNull Boolean isCurrencyEnabled() {
        return currencyEnabled;
    }
    @Override
    public void setCurrencyEnabled(@NotNull Boolean currencyEnabled) {
        this.currencyEnabled = currencyEnabled;
    }
    @Override
    public @NotNull Boolean isTestingEnabled() {
        return testingEnabled;
    }
    @Override
    public void setTestingEnabled(@NotNull Boolean testingEnabled) {
        this.testingEnabled = testingEnabled;
    }
}
