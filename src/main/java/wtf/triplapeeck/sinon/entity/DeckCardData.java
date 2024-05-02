package wtf.triplapeeck.sinon.entity;

import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.cards.Face;
import wtf.triplapeeck.sinon.cards.Suit;

public abstract class DeckCardData extends AccessibleDataEntity {
    public abstract @NotNull Face getFace();
    public abstract void setFace(@NotNull Face face);
    public abstract @NotNull Suit getSuit();
    public abstract void setSuit(@NotNull Suit suit);
    public abstract Integer getIndex();
    public abstract void setIndex(Integer index);
    public abstract @NotNull ChannelData getChannel();
    public abstract void setChannel(@NotNull ChannelData channel);
}
