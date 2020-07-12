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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UpgradeGUI {

    public static void Open(Gangs main, Player p){
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_AQUA + "Gangs - Upgrades");

        ItemStack[] Slots = new ItemStack[27];


        ItemStack Blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta Blue_Meta = Blue.getItemMeta();
        Blue_Meta.setDisplayName("");
        Blue.setItemMeta(Blue_Meta);

        ItemStack LBlue = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, (short) 3);
        ItemMeta LBlue_Meta = Blue.getItemMeta();
        LBlue_Meta.setDisplayName("");
        Blue.setItemMeta(LBlue_Meta);

        Gang gang = main.GetMember(p).getGang();

        ItemStack WealthShare = new ItemStack(Material.EMERALD,1);
        ItemMeta WealthShare_meta = WealthShare.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_AQUA + "Gives a % of the Money you make");
        lore.add(ChatColor.DARK_AQUA + "to your Team Mates!");

        lore.add(ChatColor.DARK_AQUA +"COST:"+ChatColor.AQUA+gang.GetWScost(main));

        WealthShare_meta.setLore(lore);
        WealthShare_meta.setDisplayName(ChatColor.AQUA + "Wealth Share");
        WealthShare.setItemMeta(WealthShare_meta);



        ItemStack MemberUpgrade = new ItemStack(Material.ARROW,1);
        ItemMeta MemberUpgrade_meta = MemberUpgrade.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_AQUA + "Allow Another Person to Join Your Gang!");


        lore.add(ChatColor.DARK_AQUA +"COST:"+ChatColor.AQUA+gang.GetMemberCost(main));

        MemberUpgrade_meta.setLore(lore);
        MemberUpgrade_meta.setDisplayName(ChatColor.AQUA + "Member Upgrade");
        MemberUpgrade.setItemMeta(MemberUpgrade_meta);


        ItemStack ComingSoon = new ItemStack(Material.BEDROCK,1);
        ItemMeta ComingSoon_meta = ComingSoon.getItemMeta();
        lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_RED + "COMING SOON");

        ComingSoon_meta.setLore(lore);
        ComingSoon_meta.setDisplayName(ChatColor.AQUA + "ComingSoon");
        ComingSoon.setItemMeta(ComingSoon_meta);

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
        Slots[10] = LBlue;
        Slots[11] = WealthShare;
        Slots[12] = LBlue;
        Slots[13] = MemberUpgrade;
        Slots[14] = LBlue;
        Slots[15] = ComingSoon;
        Slots[16] = LBlue;
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
