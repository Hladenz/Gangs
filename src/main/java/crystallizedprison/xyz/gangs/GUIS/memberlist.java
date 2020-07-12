package crystallizedprison.xyz.gangs.GUIS;

import crystallizedprison.xyz.gangs.Gang;
import crystallizedprison.xyz.gangs.Gangs;
import me.clip.ezblocks.EZBlocks;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class memberlist {

    public static ItemStack GetUUIDHead(String displayname, String HeadUUID, ArrayList<String> lore) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(displayname);
        skull.setLore(lore);
        skull.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(HeadUUID)));
        item.setItemMeta(skull);
        return item;
    }

    public static void Open(Gangs main, Player p){
        Inventory inv = Bukkit.createInventory(null, 54, ChatColor.DARK_AQUA + "Gangs - Member List");
        Gang gang = main.GetMember(p).getGang();

        List<ItemStack> invitems = new ArrayList<>();
        for (String UUID:main.GetMember(p).getGang().getMembers() ){


            Date date = new Date(p.getLastPlayed());
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String Seen = sdf.format(date);

            ArrayList<String> lore = new ArrayList<String>();
            if (gang.getBlocksMinedContributed().containsKey(UUID)) {
                lore.add(ChatColor.AQUA + "BlocksMined: " + ChatColor.DARK_AQUA + gang.getBlocksMinedContributed().get(UUID));
            }
            if (gang.getSharedIncomeContributed().containsKey(UUID)) {
                lore.add(ChatColor.AQUA + "Wealth Share: " + ChatColor.DARK_AQUA + gang.getSharedIncomeContributed().get(UUID));
            }
            lore.add(ChatColor.AQUA + "Last Seen: " + ChatColor.DARK_AQUA + Seen);
            lore.add(ChatColor.AQUA + "Click For more Options");

            ItemStack playerhead = GetUUIDHead(ChatColor.AQUA +Bukkit.getOfflinePlayer(java.util.UUID.fromString(UUID)).getName(),UUID,lore);

            invitems.add(playerhead);

        }

        inv.setContents(invitems.toArray(new ItemStack[53]));
        p.openInventory(inv);

    }
}
