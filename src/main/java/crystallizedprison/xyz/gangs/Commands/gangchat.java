package crystallizedprison.xyz.gangs.Commands;

import crystallizedprison.xyz.gangs.Gangs;
import crystallizedprison.xyz.gangs.Member;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class gangchat implements CommandExecutor {

    public gangchat(Gangs main) {
        this.main = main;
    }

    private Gangs main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = ((Player) sender).getPlayer();

            Member member = main.GetMember(p);

            if (member.getGang() == null){
                p.sendMessage(ChatColor.DARK_AQUA +"You Need to be in a gang to use gang chat!");
                return true;
            }

            if (args.length == 0){
                p.sendMessage(ChatColor.DARK_AQUA + "Please add a message to send");
                return true;
            }

            member.getGang().SendMessageToGang(ChatColor.AQUA + p.getName() +ChatColor.DARK_AQUA +":"+String.join(" ",args),main);

        }

        return false;
    }
}
