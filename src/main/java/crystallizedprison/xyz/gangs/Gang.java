package crystallizedprison.xyz.gangs;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import xyz.crystallizedprison.petssystem.Pet;
import xyz.crystallizedprison.petssystem.PetsSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Gang {

    public Gang(String name, String tag, int avaliableBlocksMined, HashMap<String, Integer> upgrades, List<String> members, String owner, HashMap<String, Integer> blocksMinedContributed, HashMap<String, Integer> sharedIncomeContributed) {
        Name = name;
        Tag = tag;
        AvaliableBlocksMined = avaliableBlocksMined;
        Upgrades = upgrades;
        Members = members;
        Owner = owner;
        BlocksMinedContributed = blocksMinedContributed;
        SharedIncomeContributed = sharedIncomeContributed;
    }

    private String Name;
    private String Tag;
    private int AvaliableBlocksMined;
    private HashMap<String,Integer> Upgrades;
    private List<String> Members;
    private String Owner;
    private HashMap<String,Integer> BlocksMinedContributed;
    private HashMap<String,Integer> SharedIncomeContributed;
    private List<Player> Invites = new ArrayList<>();

    public String getName() {
        return Name;
    }
    public String getTag() {
        return Tag;
    }
    public int getAvaliableBlocksMined() {
        return AvaliableBlocksMined;
    }
    public HashMap<String, Integer> getUpgrades() {
        return Upgrades;
    }
    public List<String> getMembers() {
        return Members;
    }
    public String getOwner() {
        return Owner;
    }
    public HashMap<String, Integer> getBlocksMinedContributed() {
        return BlocksMinedContributed;
    }
    public HashMap<String, Integer> getSharedIncomeContributed() {
        return SharedIncomeContributed;
    }
    public List<Player> getInvites() {
        return Invites;
    }

    public void setOwner(String owner) {
        Owner = owner;
    }
    public void setTag(String tag) {
        Tag = tag;
    }
    public void setAvaliableBlocksMined(int avaliableBlocksMined) {
        AvaliableBlocksMined = avaliableBlocksMined;
    }
    public void setUpgrades(HashMap<String, Integer> upgrades) {
        Upgrades = upgrades;
    }
    public void setMembers(List<String> members) {
        Members = members;
    }
    public void setBlocksMinedContributed(HashMap<String, Integer> blocksMinedContributed) {
        BlocksMinedContributed = blocksMinedContributed;
    }
    public void setSharedIncomeContributed(HashMap<String, Integer> sharedIncomeContributed) {
        SharedIncomeContributed = sharedIncomeContributed;
    }

    public List<Player> GetOnlineMembers(){
        List<Player> onlineMembers = new ArrayList<>();
        for (Player player:Bukkit.getOnlinePlayers()){
            if (Members.contains(player.getUniqueId().toString())){
                onlineMembers.add(player);
            }
        }
        return onlineMembers;
    }


    public void SendMessageToGang(String message,Gangs gangs){
        List<Player> onlinemembers = this.GetOnlineMembers();
        for (Player member:onlinemembers){
            member.sendMessage(ChatColor.BLACK + "["+ChatColor.AQUA +"GANG CHAT" + ChatColor.BLACK + "] " + message);
        }

        for(Player staff:gangs.getGangspy()){
            staff.sendMessage(ChatColor.BLACK + "["+ChatColor.AQUA +"GANG CHAT - "+this.Name + ChatColor.BLACK + "] " + message);
        }
    }

    public int GetWScost(Gangs main){
        if (!this.Upgrades.containsKey("ws")){

            int cost =main.getConfig().getInt("upgrades.ws.cost");
            if(this.getUpgrades().containsKey("member")) {
                cost *= (this.getUpgrades().get("member") * 0.08);
            }
            return cost;

        }else{

            int cost =(int) Math.round((main.getConfig().getInt("upgrades.ws.cost"))*this.Upgrades.get("ws") * ((0.1*this.Upgrades.get("ws"))+1));
            if(this.getUpgrades().containsKey("member")) {
                cost *= (this.getUpgrades().get("member")*0.08)+1;
            }

            return cost;
        }

    }

    public String[] GetMemberNames(){
        String[] Names = new String[Members.size()];
        for (String uuid:Members){
            Names[Members.indexOf(uuid)] = Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getName();
        }
        return Names;
    }

    public int GetMemberCost(Gangs main){
        if (!this.Upgrades.containsKey("member")){
            int cost =main.getConfig().getInt("upgrades.members.cost");
            return cost;
        }else{
            int cost =(int) Math.round((main.getConfig().getInt("upgrades.members.cost"))*this.Upgrades.get("member") * ((0.1*this.Upgrades.get("member"))+1));
            return cost;
        }
    }

    public int GetTotalWS(){
        int Total = 0;
        for (String uuid:this.SharedIncomeContributed.keySet()){
            Total += this.SharedIncomeContributed.get(uuid);
        }
        return Total;
    }

    public int GetTotalBlocks(){
        int Total = 0;
        for (String uuid:this.BlocksMinedContributed.keySet()){
            Total += this.BlocksMinedContributed.get(uuid);
        }
        return Total;
    }

    public int MaxMembers(){
        if(!Upgrades.containsKey("member")){
            return 3;
        }else{
            return 3 + (Upgrades.get("member"));
        }
    }

    private PetsSystem getPets() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PetsSystem");
        if ((plugin == null)) {
            return null;
        }
        return (xyz.crystallizedprison.petssystem.PetsSystem)plugin;
    }

    public void GiveMoney(double money,Player player){
        List<Player> Players= this.GetOnlineMembers();
        Players.remove(player);
        if (Players != null){
            double amount = money / (double) Players.size();
            for (Player p:Players){
                PetsSystem petsSystem = getPets();
                if (petsSystem.GetPlayerInfo(p).getPet() != null) {
                    if (petsSystem.GetPlayerInfo(p).getPet().equals(Pet.MAFIA)) {
                        Gangs.getEcon().depositPlayer(p,amount*2);
                    }else {
                        Gangs.getEcon().depositPlayer(p,amount);
                    }
                }else {
                    Gangs.getEcon().depositPlayer(p,amount);
                }
            }
        }
    }
}
