package wtf.triplapeeck.sinon.entity;

import wtf.triplapeeck.sinon.cards.BlackjackState;

import java.util.Collection;

public abstract class ChannelData extends AccessibleDataEntity {
    public abstract Boolean getAutoThread();
    public abstract void setAutoThread(Boolean autoThread);
    public abstract Boolean getBlackjackInProgress();
    public abstract void setBlackjackInProgress(Boolean blackjackInProgress);
    public abstract BlackjackState getBlackjackState();
    public abstract void setBlackjackState(BlackjackState blackjackState);
    public abstract Collection<? extends PlayerSpotData> getPlayerSpots();
    public abstract Collection<? extends DeckCardData> getDeck();
    public abstract Collection<? extends PlayerCardData> getPlayerCards();
}
