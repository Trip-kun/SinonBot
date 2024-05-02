package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.ChannelData;
import wtf.triplapeeck.sinon.entity.MemberData;
import wtf.triplapeeck.sinon.entity.PlayerSpotData;

import java.util.Collection;

@DatabaseTable(tableName = "oatmeal_player_spots")
public class ORMLitePlayerSpotData extends PlayerSpotData {
    @DatabaseField(generatedId = true)
    private @NotNull String id;
    @DatabaseField(canBeNull=false)
    private @NotNull Integer bet;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean insured;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean doubled;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean doubled2;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean split;
    @ForeignCollectionField(eager = true)
    private @NotNull Collection<ORMLitePlayerCardData> cardData;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private @NotNull ORMLiteChannelData channel;
    @DatabaseField(canBeNull = false, foreign = true, foreignAutoRefresh = true)
    private @NotNull ORMLiteMemberData player;
    public ORMLitePlayerSpotData() {}
    public ORMLitePlayerSpotData(String ignored) {
    }
    @Override
    public @NotNull Integer getBet() {
        return bet;
    }
    @Override
    public @NotNull Boolean getInsured() {
        return insured;
    }
    @Override
    public @NotNull Boolean getDoubled() {
        return doubled;
    }
    @Override
    public @NotNull Boolean getDoubled2() {
        return doubled2;
    }
    @Override
    public @NotNull Boolean getSplit() {
        return split;
    }
    @Override
    public @NotNull ORMLiteChannelData getChannel() {
        return channel;
    }
    @Override
    public @NotNull ORMLiteMemberData getPlayer() {
        return player;
    }
    public @NotNull Collection<? extends ORMLitePlayerCardData> getCards() {
        return cardData;
    }
    @Override
    public void setBet(@NotNull Integer bet) {
        this.bet = bet;
    }
    @Override
    public void setInsured(@NotNull Boolean insured) {
        this.insured = insured;
    }
    @Override
    public void setDoubled(@NotNull Boolean doubled) {
        this.doubled = doubled;
    }
    @Override
    public void setDoubled2(@NotNull Boolean doubled2) {
        this.doubled2 = doubled2;
    }
    @Override
    public void setSplit(@NotNull Boolean split) {
        this.split = split;
    }
    @Override
    public void setChannel(@NotNull ChannelData channel) {
        this.channel = (ORMLiteChannelData) channel;
    }
    @Override
    public void setPlayer(@NotNull MemberData player) {
        this.player = (ORMLiteMemberData) player;
    }
    @Override
    public void load() {
    }
    @Override
    public @NotNull String getID() {
        return id;
    }
}
