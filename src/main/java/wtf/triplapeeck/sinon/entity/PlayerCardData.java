package wtf.triplapeeck.sinon.entity;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.cards.Face;
import wtf.triplapeeck.sinon.cards.Suit;

public abstract class PlayerCardData extends AccessibleDataEntity {
    public abstract @NotNull Face getFace();
    public abstract void setFace(@NotNull Face face);
    public abstract @NotNull Suit getSuit();
    public abstract void setSuit(@NotNull Suit suit);
    public abstract @NotNull ChannelData getChannel();
    public abstract void setChannel(@NotNull ChannelData channel);
    public abstract @NotNull PlayerSpotData getPlayer();
    public abstract void setPlayer(@NotNull PlayerSpotData player);
}
