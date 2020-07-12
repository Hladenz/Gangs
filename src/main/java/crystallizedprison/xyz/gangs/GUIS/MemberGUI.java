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

public class MemberGUI {


    public static void Open(Gangs main, Player p){
        Inventory inv = Bukkit.createInventory(null, 27, ChatColor.DARK_AQUA + "Gangs - Member Gui");

        ItemStack[] Slots = new ItemStack[27];


        ItemStack Blue = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1, (short) 11);
        ItemMeta Blue_Meta = Blue.getItemMeta();
        Blue_Meta.setDisplayName("");
        Blue.setItemMeta(Blue_Meta);

        ItemStack LBlue = new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE, 1, (short) 3);
        ItemMeta LBlue_Meta = Blue.getItemMeta();
        LBlue_Meta.setDisplayName("");
        Blue.setItemMeta(LBlue_Meta);

        ItemStack ComingSoon = new ItemStack(Material.BEDROCK,1);
        ItemMeta ComingSoon_meta = ComingSoon.getItemMeta();
        ArrayList<String> lore = new ArrayList<String>();
        lore.add(ChatColor.DARK_RED + "COMING SOON");

        ComingSoon_meta.setLore(lore);
        ComingSoon_meta.setDisplayName(ChatColor.AQUA + "ComingSoon");
        ComingSoon.setItemMeta(ComingSoon_meta);

        Gang gang = main.GetMember(p).getGang();

        Date date = new Date(p.getLastPlayed());
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String Seen = sdf.format(date);

        ItemStack Kick = new ItemStack(Material.BARRIER,1);
        ItemMeta Kick_meta = Kick.getItemMeta();
        lore = new ArrayList<String>();
        if (gang.getOwner().equals(p.getUniqueId().toString())) {
            lore.add(ChatColor.DARK_RED + "You Cant kick your self stupid!");
        }else{
            lore.add(ChatColor.DARK_RED + "Click This Button to kick someone from that gang");
        }

        Kick_meta.setLore(lore);
        Kick_meta.setDisplayName(ChatColor.AQUA + "Kick");
        Kick.setItemMeta(Kick_meta);

        ItemStack Stats = new ItemStack(Material.CLOCK,1);
        ItemMeta Stats_meta = Stats.getItemMeta();
        lore = new ArrayList<String>();
        if (gang.getBlocksMinedContributed().containsKey(p.getUniqueId().toString())) {
            lore.add(ChatColor.AQUA + "BlocksMined: " + ChatColor.DARK_AQUA + gang.getBlocksMinedContributed().get(p.getUniqueId().toString()));
        }
        if (gang.getSharedIncomeContributed().containsKey(p.getUniqueId().toString())) {
            lore.add(ChatColor.AQUA + "Wealth Share: " + ChatColor.DARK_AQUA + gang.getSharedIncomeContributed().get(p.getUniqueId().toString()));
        }
        lore.add(ChatColor.AQUA + "Last Seen: " + ChatColor.DARK_AQUA + Seen);

        Stats_meta.setLore(lore);
        Stats_meta.setDisplayName(ChatColor.AQUA + "Stats");
        Stats.setItemMeta(Stats_meta);


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
        Slots[11] = ComingSoon;
        Slots[12] = LBlue;
        Slots[13] = Stats;
        Slots[14] = LBlue;
        Slots[15] = Kick;
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
