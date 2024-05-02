package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public abstract class UserData extends AccessibleDataEntity {
    public abstract @NotNull Boolean isOwner();
    public abstract void setOwner(@NotNull Boolean owner);
    public abstract @NotNull Boolean isAdmin();
    public abstract void setAdmin(@NotNull Boolean admin);
    public abstract @NotNull Boolean isCurrencyPreferenceOn();
    public abstract void setCurrencyPreferenceOn(@NotNull Boolean currencyPreferenceOn);
    public abstract Collection<? extends ReminderData> getReminders();
}
