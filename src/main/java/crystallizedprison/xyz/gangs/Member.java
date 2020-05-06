package crystallizedprison.xyz.gangs;

public class Member {


    public Member(Gang gang, String UUID, boolean created, int redeemed) {
        this.gang = gang;
        this.UUID = UUID;
        Created = created;
        Redeemed = redeemed;
    }

    private Gang gang;
    private String UUID;
    private boolean Created;
    private int Redeemed;

    public String getUUID() {
        return UUID;
    }

    public Gang getGang() {
        return gang;
    }
    public void setGang(Gang gang) {
        this.gang = gang;
    }

    public boolean isCreated() {
        return Created;
    }
    public void setCreated(boolean created) {
        Created = created;
    }

    public int getRedeemed() {
        return Redeemed;
    }
    public void setRedeemed(int redeemed) {
        Redeemed = redeemed;
    }

}
