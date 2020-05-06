package crystallizedprison.xyz.gangs.Commands;

import crystallizedprison.xyz.gangs.Gang;
import crystallizedprison.xyz.gangs.Gangs;
import crystallizedprison.xyz.gangs.Member;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class gangs implements CommandExecutor {

    public gangs(Gangs Main) {
        main = Main;
    }

    Gangs main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player){
            Player p = ((Player) sender).getPlayer();
            if (!p.hasPermission("gangs.admin")){
                p.sendMessage(ChatColor.AQUA +"This is the Admin Command Please use /gang Instead");
                return true;
            }

            if(args.length != 0){
                if (args[0].toLowerCase().equals("forcejoin")){
                    if (args.length <2 ){
                        p.sendMessage(ChatColor.AQUA +"Invalid fromat /gangs forcejoin [gang name]");
                        return true;
                    }
                    List<String> name = new ArrayList<>();
                    for (String arg:args){
                        name.add(arg);
                    }
                    name.remove(0);
                    String GangName;
                    if (name.size() != 1){
                        GangName = String.join(" ",name);
                    }else{
                        GangName = name.get(0);
                    }
                    System.out.println("Gang Name:"+GangName);
                    if (main.GetGangByName(GangName) == null){
                        p.sendMessage(ChatColor.AQUA + "Please provide a valid Gang Name");
                        return true;
                    }

                    Gang gang = main.GetGangByName(String.join(" ",name));

                    Member member = main.GetMember(p);
                    if (member.getGang() != null){
                        p.sendMessage(ChatColor.AQUA + "You must not be in a gang to Join a Gang, Please Leave your Current Gang(/gang Leave)!");
                        return true;
                    }

                    member.setGang(gang);
                    main.SaveMember(member);

                    p.sendMessage(ChatColor.DARK_AQUA + "You have successfully Joined " + ChatColor.AQUA + gang.getName());
                    gang.SendMessageToClan(ChatColor.AQUA + p.getName() + ChatColor.DARK_AQUA +" Has Joined the Clan");
                    gang.getMembers().add(p.getUniqueId().toString());
                    main.Save();
                    return true;
                }
                else if(args[0].toLowerCase().equals("kick")){
                    Member member = main.GetMember(p);

                    if (member.getGang() == null){
                        p.sendMessage(ChatColor.AQUA + "You need to be the Leader of a Gang to kick People");
                        return true;
                    }

                    if (args.length == 1){
                        p.sendMessage(ChatColor.DARK_AQUA + "Please Add what member you want to kick /gang kick [player]");
                        return true;
                    }

                    if (Bukkit.getPlayer(args[1])== null){
                        p.sendMessage(ChatColor.DARK_AQUA + "Please add a valid member you want to kick /gang kick [player]");
                        return true;
                    }




                    Player kick = Bukkit.getPlayer(args[1]);
                    Gang gang = member.getGang();

                    if (!gang.getMembers().contains(kick.getUniqueId().toString())){
                        p.sendMessage(ChatColor.DARK_AQUA + "This Player Has to be in the gang to kick them");
                        return true;
                    }

                    Member kick_member = main.GetMember(kick);


                    gang.getMembers().remove(kick.getUniqueId().toString());
                    kick_member.setGang(null);
                    main.SaveMember(kick_member);
                    main.Save();
                    kick.sendMessage(ChatColor.AQUA+"You Have been kicked from the Gang!");
                    gang.SendMessageToClan(ChatColor.AQUA + kick.getName() + ChatColor.DARK_AQUA + " Has been Kicked from the gang!");
                    return true;
                }
                else if (args[0].toLowerCase().equals("list")){
                    List<String> message = new ArrayList<>();
                    message.add(ChatColor.AQUA + "Gangs:");
                    for (Gang gang:main.getGangs()) {
                        message.add(ChatColor.DARK_AQUA + "Name:" +gang.getName() + " Tag:"+gang.getTag() +" Size:"+gang.getMembers().size());
                    }
                    p.sendMessage(message.toArray(new String[message.size()]));
                }
                else if(args[0].toLowerCase().equals("disband")){

                    if (args.length < 2){
                        p.sendMessage(ChatColor.DARK_AQUA +"Invalid Format");
                        return true;
                    }
                    List<String> name = new ArrayList<>();
                    for (String arg:args){
                        name.add(arg);
                    }
                    name.remove(0);
                    String GangName;
                    if (name.size() != 1){
                        GangName = String.join(" ",name);
                    }else{
                        GangName = name.get(0);
                    }
                    System.out.println("Gang Name:"+GangName);
                    if (main.GetGangByName(GangName) == null){
                        p.sendMessage(ChatColor.AQUA + "Please provide a valid Gang Name");
                        return true;
                    }

                    Gang gang = main.GetGangByName(String.join(" ",name));

                    for(String uuid:gang.getMembers()){
                        Member member = main.GetMember(Bukkit.getOfflinePlayer(UUID.fromString(uuid)).getPlayer());
                        member.setGang(null);
                        main.SaveMember(member);

                    }
                    main.getGangs().remove(gang);
                    main.Save();
                    return true;

                }
                else if(args[0].toLowerCase().equals("setowner")){
                    Member member = main.GetMember(p);
                    if (member.getGang() == null){
                        p.sendMessage(ChatColor.AQUA + "You need to be the Leader of a Clan to Invite People");
                        return true;
                    }

                    if (args.length == 1){
                        p.sendMessage(ChatColor.DARK_AQUA + "Please Add what member you want to invite /gang invite [player]");
                        return true;
                    }

                    if (Bukkit.getPlayer(args[1])== null){
                        p.sendMessage(ChatColor.DARK_AQUA + "Please add a valid member you want to invite /gang invite [player]");
                        return true;
                    }

                    if (member.getGang().getMembers().size() >= member.getGang().MaxMembers()){
                        p.sendMessage(ChatColor.DARK_AQUA + "You Have Reached the Max Gang Members, You can Upgrade this!");
                        return true;
                    }



                    Player newowner = Bukkit.getPlayer(args[1]);

                    if (main.GetMember(newowner).getGang() != member.getGang()){
                        p.sendMessage(ChatColor.AQUA + "the New owner has to be in the gang your trying to replace ownership off");
                        return true;
                    }

                    member.getGang().setOwner(newowner.getUniqueId().toString());
                    main.Save();
                }
                else if(args[0].toLowerCase().equals("debug")){
                    List<String> name = new ArrayList<>();
                    for (String arg:args){
                        name.add(arg);
                    }
                    name.remove(0);
                    String GangName;
                    if (name.size() != 1){
                        GangName = String.join(" ",name);
                    }else{
                        GangName = name.get(0);
                    }
                    System.out.println("Gang Name:"+GangName);
                    if (main.GetGangByName(GangName) == null){
                        p.sendMessage(ChatColor.AQUA + "Please provide a valid Gang Name");
                        return true;
                    }

                    Gang gang = main.GetGangByName(String.join(" ",name));

                    List<String> mesage = new ArrayList<>();
                    mesage.add(ChatColor.DARK_AQUA + "Gang name:"+ChatColor.AQUA+gang.getName());
                    mesage.add(ChatColor.DARK_AQUA + "Tag:"+ChatColor.AQUA+gang.getTag());
                    mesage.add(ChatColor.DARK_AQUA + "Members:"+ChatColor.AQUA+gang.getMembers().size());
                    mesage.add(ChatColor.DARK_AQUA + "Total WS:"+ChatColor.AQUA +gang.GetTotalWS());
                    mesage.add(ChatColor.DARK_AQUA + "Total Blocks Mined:"+ChatColor.AQUA +gang.GetTotalBlocks());
                    mesage.add(ChatColor.DARK_AQUA + "maxMembers:"+ChatColor.AQUA +gang.MaxMembers());
                    mesage.add(ChatColor.DARK_AQUA + "ABM:"+ChatColor.AQUA +gang.getAvaliableBlocksMined());
                    if(gang.getUpgrades().containsKey("member")){
                        mesage.add(ChatColor.DARK_AQUA + "member_level:"+ChatColor.AQUA +gang.getUpgrades().get("member"));
                    }
                    mesage.add(ChatColor.DARK_AQUA + "Go /debug info [name] - to See Other Gangs info");
                    p.sendMessage(mesage.toArray(new String[mesage.size()]));
                }
            }
        }

        return false;
    }
}
