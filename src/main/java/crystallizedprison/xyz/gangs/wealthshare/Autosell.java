package crystallizedprison.xyz.gangs.wealthshare;

import crystallizedprison.xyz.gangs.Gang;
import crystallizedprison.xyz.gangs.Gangs;
import crystallizedprison.xyz.gangs.Member;
import me.clip.autosell.events.AutoSellAnnounceEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class Autosell implements Listener {

    public Autosell(Gangs main) {
        this.main = main;
    }

    Gangs main;

    @EventHandler(ignoreCancelled = true)
    public void onAutoSellAnnounce(AutoSellAnnounceEvent event) {
        Member member = main.GetMember(event.getPlayer());
        Player p = event.getPlayer();

        if (member.getGang() != null){
            Gang gang = member.getGang();
            if (gang.getUpgrades().containsKey("ws")){
                int level = gang.getUpgrades().get("ws");
                gang.GiveMoney(event.getTotal()*(0.01*level),p);
                System.out.println(event.getTotal());
                if (!gang.GetOnlineMembers().isEmpty()) {
                    if (gang.getSharedIncomeContributed().containsKey(p.getUniqueId().toString())) {
                        gang.getSharedIncomeContributed().put(p.getUniqueId().toString(), gang.getSharedIncomeContributed().get(p.getUniqueId().toString()) + (int) (event.getTotal() * (0.01 * level)));
                    } else {
                        gang.getSharedIncomeContributed().put(p.getUniqueId().toString(), (int) (event.getTotal() * (0.01 * level)));
                    }
                }
            }
        }
    }
}
