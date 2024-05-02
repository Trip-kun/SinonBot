package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.ReminderData;
import wtf.triplapeeck.sinon.entity.UserData;

@DatabaseTable(tableName = "oatmeal_reminders")
public class ORMLiteReminderData extends ReminderData {
    @DatabaseField(generatedId = true)
    private String id;
    @DatabaseField(canBeNull=false, foreign = true, foreignAutoRefresh = true)
    private ORMLiteUserData user;
    @DatabaseField(canBeNull=false, width=5000)
    private String message;
    @DatabaseField(canBeNull=false)
    private Long time;
    public ORMLiteReminderData(String ignored) {}
    public ORMLiteReminderData() {}
    @Override
    public @NotNull String getReminderText() {
        return message;
    }
    @Override
    public void setReminderText(@NotNull String reminderText) {
        this.message = reminderText;
    }
    @Override
    public @NotNull Long getReminderTimestamp() {
        return time;
    }
    @Override
    public void setReminderTimestamp(@NotNull Long reminderTimestamp) {
        this.time = reminderTimestamp;
    }
    @Override
    public @NotNull UserData getUser() {
        return user;
    }
    @Override
    public void setUser(@NotNull UserData user) {
        this.user = (ORMLiteUserData) user;
    }
    @Override
    public @NotNull String getID() {
        return id;
    }
    @Override
    public void load() {
    }
}
