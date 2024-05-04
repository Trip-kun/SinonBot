package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.UserData;

import java.util.ArrayList;
import java.util.Collection;

@DatabaseTable(tableName = "oatmeal_users")
public class ORMLiteUserData extends UserData {
    @DatabaseField(id = true)
    private @NotNull String id;
    @DatabaseField(canBeNull = false)
    private @NotNull Boolean owner;
    @DatabaseField(canBeNull = false)
    private @NotNull Boolean admin;
    @DatabaseField(canBeNull = false)
    private @NotNull Boolean currencyPreference;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLiteReminderData> reminders;
    public ORMLiteUserData(String id) {
        this.id = id;
        this.owner = false;
        this.admin = false;
        this.currencyPreference = false;
        reminders = new ArrayList<>();
    }
    public ORMLiteUserData() {} // For ORMLite

    @Override
    public void load() {

    }
    @Override
    public @NotNull String getID() {
        return id;
    }
    @Override
    public @NotNull Boolean isOwner() {
        return owner;
    }

    @Override
    public void setOwner(@NotNull Boolean owner) {
        this.owner = owner;
    }
    @Override
    public @NotNull Boolean isAdmin() {
        return admin;
    }
    @Override
    public void setAdmin(@NotNull Boolean admin) {
        this.admin = admin;
    }
    @Override
    public @NotNull Boolean isCurrencyPreferenceOn() {
        return currencyPreference;
    }
    @Override
    public Collection<? extends ORMLiteReminderData> getReminders() {
        return reminders;
    }
    @Override
    public void setCurrencyPreferenceOn(@NotNull Boolean currencyPreferenceOn) {
        this.currencyPreference = currencyPreferenceOn;
    }
}
