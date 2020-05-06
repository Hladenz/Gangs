package crystallizedprison.xyz.gangs.Events;

import crystallizedprison.xyz.gangs.GUIS.MemberGUI;
import crystallizedprison.xyz.gangs.GUIS.UpgradeGUI;
import crystallizedprison.xyz.gangs.GUIS.memberlist;
import crystallizedprison.xyz.gangs.Gang;
import crystallizedprison.xyz.gangs.Gangs;
import crystallizedprison.xyz.gangs.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class InventoryManager implements Listener {

    public InventoryManager(Gangs main) {
        this.main = main;
    }

    Gangs main;
    HashMap<Player,Player> OpenedGUIS = new HashMap<>();


    @EventHandler(ignoreCancelled = true)
    public void onInventoryInteract(InventoryClickEvent event) {
        if (event.getInventory().getTitle().contains("Clans -")){
            event.setCancelled(true);
            if (event.getInventory().getTitle().contains("Main menu")){
                ItemStack clicked = event.getCurrentItem();
                Player player = (Player) event.getWhoClicked();

                if (clicked.getType().equals(Material.BOOK)){
                    memberlist.Open(main,player);
                }else if (clicked.getType().equals(Material.ANVIL)){
                    UpgradeGUI.Open(main,player);
                }
            }else if (event.getInventory().getTitle().contains("Member List")){
                ItemStack clicked = event.getCurrentItem();
                Player player = (Player) event.getWhoClicked();
                int slot = event.getSlot()-1;

                MemberGUI.Open(main, Bukkit.getOfflinePlayer(UUID.fromString(main.GetMember(player).getGang().getMembers().get(slot))).getPlayer());
                OpenedGUIS.put(player,Bukkit.getOfflinePlayer(UUID.fromString(main.GetMember(player).getGang().getMembers().get(slot))).getPlayer());
            }
            else if (event.getInventory().getTitle().contains("Member Gui")){
                ItemStack clicked = event.getCurrentItem();
                Player player = (Player) event.getWhoClicked();
                int slot = event.getSlot();

                if (clicked.getType().equals(Material.BARRIER)) {
                    Player kick = OpenedGUIS.get(player);

                    Member kick_member = main.GetMember(kick);
                    Gang gang = main.GetMember(player).getGang();

                    gang.getMembers().remove(kick.getUniqueId().toString());
                    kick_member.setGang(null);
                    main.SaveMember(kick_member);
                    main.Save();
                    if (kick.isOnline()) {
                        kick.sendMessage(ChatColor.AQUA + "You Have been kicked from the Gang!");
                    }
                    gang.SendMessageToClan(ChatColor.AQUA + kick.getName() + ChatColor.DARK_AQUA + " Has been Kicked from the gang!");
                }
            }
            else if (event.getInventory().getTitle().contains("Upgrades")){
                ItemStack clicked = event.getCurrentItem();
                Player player = (Player) event.getWhoClicked();

                if (clicked.getType().equals(Material.ARROW)){
                    int cost = 0;
                    Gang gang = main.GetMember(player).getGang();

                    if (!gang.getUpgrades().containsKey("member")){
                        cost =main.getConfig().getInt("upgrades.members.cost");
                    }else{
                        cost = (main.getConfig().getInt("upgrades.members.cost"))*gang.getUpgrades().get("member");
                    }

                    if (gang.getAvaliableBlocksMined() < cost){
                        player.closeInventory();
                        player.sendMessage(ChatColor.DARK_AQUA +"You Need " + cost + " Blocks Mined Available");
                        return;
                    }

                    gang.setAvaliableBlocksMined(gang.getAvaliableBlocksMined()-cost);
                    if (!gang.getUpgrades().containsKey("member")){
                        gang.getUpgrades().put("member",1);
                    }else{
                        gang.getUpgrades().put("member",gang.getUpgrades().get("member")+1);
                    }
                    main.Save();
                    gang.SendMessageToClan(ChatColor.BOLD+""+ChatColor.AQUA+"Member Capacity Upgrade has just been Upgraded to " + gang.getUpgrades().get("member"));
                }
            }
        }
    }
}
