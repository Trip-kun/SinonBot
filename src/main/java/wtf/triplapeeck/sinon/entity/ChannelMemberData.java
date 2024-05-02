package wtf.triplapeeck.sinon.entity;

import java.math.BigInteger;

public abstract class ChannelMemberData extends AccessibleDataEntity{
    public abstract BigInteger getBet();
    public abstract void setBet(BigInteger bet);
    public abstract Boolean getInsured();
    public abstract void setInsured(Boolean insured);
    public abstract Boolean getDoubled();
    public abstract void setDoubled(Boolean doubled);
    public abstract Boolean getSplit();
    public abstract void setSplit(Boolean split);
    public abstract Boolean getDoubled2();
    public abstract void setDoubled2(Boolean doubled2);
}
