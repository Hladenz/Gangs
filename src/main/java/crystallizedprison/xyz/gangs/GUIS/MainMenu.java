package crystallizedprison.xyz.gangs.GUIS;

import crystallizedprison.xyz.gangs.Gang;
import crystallizedprison.xyz.gangs.Gangs;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MainMenu {

    public static void Open(Gangs main, Player p){
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_AQUA + "Clans - Main menu");

        ItemStack[] Slots = new ItemStack[27];


        ItemStack Blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta Blue_Meta = Blue.getItemMeta();
        Blue_Meta.setDisplayName("");
        Blue.setItemMeta(Blue_Meta);

        ItemStack LBlue = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, (short) 3);
        ItemMeta LBlue_Meta = Blue.getItemMeta();
        LBlue_Meta.setDisplayName("");
        Blue.setItemMeta(LBlue_Meta);

        ItemStack MemeberList = new ItemStack(Material.BOOK,1);
        ItemMeta MemeberList_META = MemeberList.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_AQUA + "See Clan Members");

        MemeberList_META.setLore(lore);
        MemeberList_META.setDisplayName(ChatColor.AQUA + "Member list");
        MemeberList.setItemMeta(MemeberList_META);

        Gang gang = main.GetMember(p).getGang();

        ItemStack Upgrades = new ItemStack(Material.ANVIL,1);
        ItemMeta Upgrades_meta = Upgrades.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_AQUA + "Upgrade What your Gang can do!");

        Upgrades_meta.setLore(lore);
        Upgrades_meta.setDisplayName(ChatColor.AQUA + "Upgrades");
        Upgrades.setItemMeta(Upgrades_meta);

        ItemStack LeaderBoards = new ItemStack(Material.IRON_BARS,1);
        ItemMeta LeaderBoards_meta = LeaderBoards.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_RED + "COMING SOON");

        LeaderBoards_meta.setLore(lore);
        LeaderBoards_meta.setDisplayName(ChatColor.AQUA + "LeaderBoards");
        LeaderBoards.setItemMeta(LeaderBoards_meta);

        ItemStack Stats = new ItemStack(Material.CLOCK,1);
        ItemMeta Stats_meta = Stats.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_AQUA + "Blocks Mined Available:"+ChatColor.AQUA +gang.getAvaliableBlocksMined());
        lore.add(ChatColor.DARK_AQUA + "Number of Members:"+ChatColor.AQUA +gang.getMembers().size());
        lore.add(ChatColor.DARK_AQUA + "Total WS:"+ChatColor.AQUA +gang.GetTotalWS());
        lore.add(ChatColor.DARK_AQUA + "Total Blocks Mined:"+ChatColor.AQUA +gang.GetTotalBlocks());

        Stats_meta.setLore(lore);
        Stats_meta.setDisplayName(ChatColor.AQUA + "Stats");
        Stats.setItemMeta(Stats_meta);

        ItemStack ClanSettings = new ItemStack(Material.WRITABLE_BOOK,1);
        ItemMeta ClanSettings_meta = ClanSettings.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_RED + "COMING SOON");

        ClanSettings_meta.setLore(lore);
        ClanSettings_meta.setDisplayName(ChatColor.AQUA + "ClanSettings");
        ClanSettings.setItemMeta(ClanSettings_meta);
        
        Slots[0] = Blue;
        Slots[1] = Blue;
        Slots[2] = LBlue;
        Slots[3] = Blue;
        Slots[4] = LBlue;
        Slots[5] = Blue;
        Slots[6] = LBlue;
        Slots[7] = Blue;
        Slots[8] = Blue;

        Slots[9] = Blue;
        Slots[10] = Blue;
        Slots[11] = MemeberList;
        Slots[12] = Upgrades;
        Slots[13] = LeaderBoards;
        Slots[14] = Stats;
        Slots[15] = ClanSettings;
        Slots[16] = Blue;
        Slots[17] = Blue;

        Slots[18] = Blue;
        Slots[19] = Blue;
        Slots[20] = LBlue;
        Slots[21] = Blue;
        Slots[22] = LBlue;
        Slots[23] = Blue;
        Slots[24] = LBlue;
        Slots[25] = Blue;
        Slots[26] = Blue;

        inv.setContents(Slots);

        p.openInventory(inv);

    }

}
