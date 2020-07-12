package crystallizedprison.xyz.gangs.wealthshare;

import crystallizedprison.xyz.gangs.Gang;
import crystallizedprison.xyz.gangs.Gangs;
import crystallizedprison.xyz.gangs.Member;
import me.clip.autosell.events.SellAllEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import xyz.crystallizedprison.petssystem.Pet;

import java.util.UUID;

public class OnSellEvent implements Listener {

    public OnSellEvent(Gangs main) {
        this.main = main;
    }

    Gangs main;

    @EventHandler(ignoreCancelled = true)
    public void onSellAll(SellAllEvent event) {
        Member member = main.GetMember(event.getPlayer());
        Player p = event.getPlayer();

        if (member.getGang() != null){
            Gang gang = member.getGang();
            if (gang.getUpgrades().containsKey("ws")){
                int level = gang.getUpgrades().get("ws");

                if (main.getPetsSystem().GetPlayerInfo(p).getPet() != null) {
                    if (main.getPetsSystem().GetPlayerInfo(p).getPet().equals(Pet.MAFIA)) {
                        gang.GiveMoney((event.getTotalCost()*(0.01*level))*2,p);
                    }else {
                        gang.GiveMoney(event.getTotalCost()*(0.01*level),p);
                    }
                }else {
                    gang.GiveMoney(event.getTotalCost()*(0.01*level),p);
                }

                if (!gang.GetOnlineMembers().isEmpty()) {
                    if (gang.getSharedIncomeContributed().containsKey(p.getUniqueId().toString())) {
                        if (main.getPetsSystem().GetPlayerInfo(p).getPet() != null) {
                            if (main.getPetsSystem().GetPlayerInfo(p).getPet().equals(Pet.MAFIA)) {
                                gang.getSharedIncomeContributed().put(p.getUniqueId().toString(), gang.getSharedIncomeContributed().get(p.getUniqueId().toString()) + (int) ((event.getTotalCost() * (0.01 * level)))*2);

                            }else {
                                gang.getSharedIncomeContributed().put(p.getUniqueId().toString(), gang.getSharedIncomeContributed().get(p.getUniqueId().toString()) + (int) (event.getTotalCost() * (0.01 * level)));
                            }
                        }else {
                            gang.getSharedIncomeContributed().put(p.getUniqueId().toString(), gang.getSharedIncomeContributed().get(p.getUniqueId().toString()) + (int) (event.getTotalCost() * (0.01 * level)));
                        }

                    } else {
                        if (main.getPetsSystem().GetPlayerInfo(p).getPet() != null) {
                            if (main.getPetsSystem().GetPlayerInfo(p).getPet().equals(Pet.MAFIA)) {
                                gang.getSharedIncomeContributed().put(p.getUniqueId().toString(),  (int) ((event.getTotalCost() * (0.01 * level)))*2);

                            }else {
                                gang.getSharedIncomeContributed().put(p.getUniqueId().toString(),  (int) (event.getTotalCost() * (0.01 * level)));
                            }
                        }else {
                            gang.getSharedIncomeContributed().put(p.getUniqueId().toString(), (int) (event.getTotalCost() * (0.01 * level)));
                        }
                    }
                }
            }
        }
    }
}
