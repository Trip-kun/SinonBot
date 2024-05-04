package wtf.triplapeeck.sinon.entity.ormlite;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;
import org.jetbrains.annotations.NotNull;
import wtf.triplapeeck.sinon.entity.ChannelMemberData;

import java.math.BigInteger;
import java.util.Collection;

@DatabaseTable(tableName = "oatmeal_channel_members")
public class ORMLiteChannelMemberData extends ChannelMemberData {
    @DatabaseField(id=true)
    private @NotNull String id;
    @DatabaseField(canBeNull=false)
    private @NotNull BigInteger bet;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean insured;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean doubled;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean split;
    @DatabaseField(canBeNull=false)
    private @NotNull Boolean doubled2;
    public ORMLiteChannelMemberData() {}
    public ORMLiteChannelMemberData(String id) {
        this.id = id;
    }
    @Override
    public @NotNull BigInteger getBet() {
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
    public @NotNull Boolean getSplit() {
        return split;
    }
    @Override
    public @NotNull Boolean getDoubled2() {
        return doubled2;
    }
    @Override
    public void setBet(@NotNull BigInteger bet) {
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
    public void setSplit(@NotNull Boolean split) {
        this.split = split;
    }
    @Override
    public void setDoubled2(@NotNull Boolean doubled2) {
        this.doubled2 = doubled2;
    }
    @Override
    public @NotNull String getID() {
        return id;
    }
    @Override
    public void load() {

    }
}
