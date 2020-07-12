package crystallizedprison.xyz.gangs;

import crystallizedprison.xyz.gangs.Commands.gang;
import crystallizedprison.xyz.gangs.Commands.gangchat;
import crystallizedprison.xyz.gangs.Commands.gangs;
import crystallizedprison.xyz.gangs.ConfigHandlers.GangsConfig;
import crystallizedprison.xyz.gangs.ConfigHandlers.MemberConfig;
import crystallizedprison.xyz.gangs.Events.InventoryManager;
import crystallizedprison.xyz.gangs.Intergrations.papi;
import crystallizedprison.xyz.gangs.wealthshare.Autosell;
import crystallizedprison.xyz.gangs.wealthshare.OnSellEvent;
import me.clip.ezblocks.EZBlocks;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;
import xyz.crystallizedprison.petssystem.PetsSystem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public final class Gangs extends JavaPlugin {

    private MemberConfig memberConfig = new MemberConfig();
    private GangsConfig gangsConfig = new GangsConfig();

    private List<Gang> gangs = new ArrayList<>();
    private List<Player> gcEnabled = new ArrayList<>();
    private List<Player> gangspy = new ArrayList<>();

    PetsSystem petsSystem;

    public MemberConfig getMemberConfig() {
        return memberConfig;
    }

    public List<Gang> getGangs() {
        return gangs;
    }

    public GangsConfig getGangsConfig() {
        return gangsConfig;
    }

    private static Economy econ = null;

    public static Economy getEcon() {
        return econ;
    }

    public List<Player> getGangspy() {
        return gangspy;
    }

    public void setGangspy(List<Player> gangspy) {
        this.gangspy = gangspy;
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }


    public PetsSystem getPets() {
        Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin("PetsSystem");
        if ((plugin == null)) {
            return null;
        }
        return (xyz.crystallizedprison.petssystem.PetsSystem)plugin;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic

        gangsConfig.setup();
        gangsConfig.get().addDefault("gangs",null);

        memberConfig.setup();
        memberConfig.get().addDefault("members",null);

        if (!setupEconomy() ) {
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getConfig().options().copyDefaults();
        saveDefaultConfig();

        getCommand("Gang").setExecutor(new gang(this));
        getCommand("Gangs").setExecutor(new gangs(this));
        getCommand("GangChat").setExecutor(new gangchat(this));

        getServer().getPluginManager().registerEvents(new InventoryManager(this), this);
        getServer().getPluginManager().registerEvents(new OnSellEvent(this), this);
        getServer().getPluginManager().registerEvents(new Autosell(this), this);

        Load();

        petsSystem=getPets();

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            /*
             * We register the EventListeneres here, when PlaceholderAPI is installed.
             * Since all events are in the main class (this class), we simply use "this"
             */
            new papi(this).register();
        } else {
            throw new RuntimeException("Could not find PlaceholderAPI!! Plugin can not work without it!");
        }
        Loop();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void Load(){
        if (gangsConfig.get().contains("gangs")) {

            for (String Index : gangsConfig.get().getConfigurationSection("gangs").getKeys(false)) {
                ConfigurationSection gangsection = gangsConfig.get().getConfigurationSection("gangs." + Index);

                String name = gangsection.getString("name");
                String tag = gangsection.getString("tag");
                int avaliableBlocksMined = gangsection.getInt("avaliableBlocksMined");
                String owner = gangsection.getString("owner");
                List<String> members = gangsection.getStringList("members");

                HashMap<String, Integer> BlocksMinedContributed = new HashMap<>();
                HashMap<String, Integer> SharedIncomeContributed = new HashMap<>();
                HashMap<String, Integer> Upgrades = new HashMap<>();

                if (gangsection.contains("upgrades")) {
                    for (String upgrade : gangsection.getConfigurationSection("upgrades").getKeys(false)) {
                        Upgrades.put(upgrade, gangsection.getInt("upgrades."+upgrade));
                    }
                }

                if (gangsection.contains("blocksMinedContributed")) {
                    for (String UUID : gangsection.getConfigurationSection("blocksMinedContributed").getKeys(false)) {
                        BlocksMinedContributed.put(UUID, gangsection.getConfigurationSection("blocksMinedContributed").getInt(UUID));
                    }
                }

                if (gangsection.contains("sharedIncomeContributed")) {
                    for (String UUID : gangsection.getConfigurationSection("sharedIncomeContributed").getKeys(false)) {
                        SharedIncomeContributed.put(UUID, gangsection.getConfigurationSection("sharedIncomeContributed").getInt(UUID));
                    }
                }

                Gang gang = new Gang(name, tag, avaliableBlocksMined, Upgrades, members, owner, BlocksMinedContributed, SharedIncomeContributed);
                System.out.println("Found Gang-"+gang.getName());
                gangs.add(gang);
            }
        }
    }

    public void Save(){
        gangsConfig.get().set("gangs",null);
        int Index=0;
        for (Gang gang:gangs){
            ConfigurationSection gangsection = gangsConfig.get().createSection("gangs."+Index);
            gangsection.set("name",gang.getName());
            gangsection.set("tag",gang.getTag());
            gangsection.set("avaliableBlocksMined",gang.getAvaliableBlocksMined());
            gangsection.set("owner",gang.getOwner());
            gangsection.set("members",gang.getMembers());

            for (String upgrade:gang.getUpgrades().keySet()){
                gangsection.set("upgrades."+upgrade,gang.getUpgrades().get(upgrade));
            }

            for (String UUID:gang.getBlocksMinedContributed().keySet()){
                gangsection.set("blocksMinedContributed."+UUID,gang.getBlocksMinedContributed().get(UUID));
            }

            for (String UUID:gang.getSharedIncomeContributed().keySet()){
                gangsection.set("sharedIncomeContributed."+UUID,gang.getSharedIncomeContributed().get(UUID));
            }


            Index++;
        }
        gangsConfig.save();
    }

    public Gang GetGangByName(String name){

        for (Gang gang:gangs){
            if (gang.getName().toLowerCase().equals(name.toLowerCase())){
                return gang;
            }
        }

        return null;
    }

    public PetsSystem getPetsSystem() {
        return petsSystem;
    }

    public Gang GetGangByTag(String tag){

        for (Gang gang:gangs){
            if (gang.getTag().toLowerCase().equals(tag.toLowerCase())){
                return gang;
            }
        }

        return null;
    }

    public Member GetMember(Player p){
        if (memberConfig.get().contains("members."+p.getUniqueId().toString() )){
            ConfigurationSection membersection = memberConfig.get().getConfigurationSection("members."+p.getUniqueId().toString());
            if (!memberConfig.get().contains("members."+p.getUniqueId().toString()+".gang")) {
                return new Member(null, p.getUniqueId().toString(), membersection.getBoolean("Created"), membersection.getInt("Crearedeemedted"));
            }else{
                return new Member(GetGangByName(membersection.getString("gang")), p.getUniqueId().toString(), membersection.getBoolean("Created"), membersection.getInt("Crearedeemedted"));
            }
        }

        return new Member(null,p.getUniqueId().toString(),false, EZBlocks.getEZBlocks().getBlocksBroken(p));
    }

    public void SaveMember(Member member){
        ConfigurationSection membersection;

        if (memberConfig.get().contains("members."+member.getUUID())){
            membersection = memberConfig.get().getConfigurationSection("members."+member.getUUID());
        }else {
            membersection = memberConfig.get().createSection("members."+member.getUUID());
        }

        if (member.getGang() == null){
            membersection.set("gang",null);
        }else {
            membersection.set("gang", member.getGang().getName());
        }
        membersection.set("Created",member.isCreated());
        membersection.set("Crearedeemedted",member.getRedeemed());
        memberConfig.save();
    }

    private void Loop(){
        BukkitScheduler scheduler = getServer().getScheduler();
        scheduler.scheduleSyncRepeatingTask(this, new Runnable() {

            @Override
            public void run() {
                for (Player p:Bukkit.getOnlinePlayers()){
                    Member member = GetMember(p);
                    if (EZBlocks.getEZBlocks().getBlocksBroken(p) > member.getRedeemed()){
                        if (member.getGang() != null){
                            Gang gang = member.getGang();
                            int toadd = EZBlocks.getEZBlocks().getBlocksBroken(p)- member.getRedeemed();

                            if (gang.getBlocksMinedContributed().containsKey(p.getUniqueId().toString())){
                                gang.getBlocksMinedContributed().put(p.getUniqueId().toString(),gang.getBlocksMinedContributed().get(p.getUniqueId().toString())+toadd);
                            }else {
                                gang.getBlocksMinedContributed().put(p.getUniqueId().toString(), toadd);
                            }
                            int NewTotal = Integer.sum(gang.getAvaliableBlocksMined(),toadd);
                            NewTotal = Math.abs(NewTotal);
                            gang.setAvaliableBlocksMined(NewTotal);
                            member.setRedeemed(EZBlocks.getEZBlocks().getBlocksBroken(p));
                            SaveMember(member);
                            Save();
                        }
                    }
                }
                Save();
            }
        }, 0L, 20L);
    }
}
