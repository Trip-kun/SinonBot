package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;

public abstract class ReminderData extends AccessibleDataEntity {
    public abstract @NotNull String getReminderText();
    public abstract void setReminderText(@NotNull String reminderText);
    public abstract @NotNull Long getReminderTimestamp();
    public abstract void setReminderTimestamp(@NotNull Long reminderTimestamp);
    public abstract @NotNull UserData getUser();
    public abstract void setUser(@NotNull UserData user);
}
