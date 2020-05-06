package crystallizedprison.xyz.gangs.Commands;

import crystallizedprison.xyz.gangs.GUIS.MainMenu;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class gang implements CommandExecutor {

    public gang(Gangs main) {
        this.main = main;
    }

    Gangs main;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player){
            Player p = ((Player) sender).getPlayer();

            if (args.length !=0){
                if (args[0].toLowerCase().equals("join")){
                    //JOIN


                    if (args.length ==1){
                        p.sendMessage(ChatColor.AQUA +"Please a clan Name that you have been invited to");
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
                    if (!gang.getInvites().contains(p)){
                        p.sendMessage(ChatColor.AQUA + "You have not been invited by the Gang Leader!");
                        return true;
                    }

                    Member member = main.GetMember(p);
                    if (member.getGang() != null){
                        p.sendMessage(ChatColor.AQUA + "You must not be in a gang to Join a Gang, Please Leave your Current Gang(/gang Leave)!");
                        return true;
                    }



                    if (gang.getMembers().size() >= gang.MaxMembers()){
                        p.sendMessage(ChatColor.DARK_AQUA + "This Gang is full!");
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
                else if (args[0].toLowerCase().equals("leave")){
                    Member member = main.GetMember(p);
                    Gang gang = member.getGang();
                    if (gang.getOwner().equals(p.getUniqueId().toString())){
                        p.sendMessage(ChatColor.AQUA + "You Can't leave your own Gang! If you Wish to disband please Contact Hladenz");
                        return true;
                    }

                    member.setGang(null);
                    main.SaveMember(member);
                    gang.getMembers().remove(p.getUniqueId().toString());
                    gang.SendMessageToClan(ChatColor.AQUA + p.getName() + ChatColor.DARK_AQUA +" Has Left the Gang");
                    p.sendMessage(ChatColor.DARK_AQUA + "You have successfully Left " + ChatColor.AQUA + gang.getName());
                    main.Save();
                    return true;
                }
                else if(args[0].toLowerCase().equals("invite")){
                    Member member = main.GetMember(p);

                    if (member.getGang() == null){
                        p.sendMessage(ChatColor.AQUA + "You need to be the Leader of a Clan to Invite People");
                        return true;
                    }else if (!member.getGang().getOwner().equals(p.getUniqueId().toString())){
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

                    Player invitie = Bukkit.getPlayer(args[1]);
                    member.getGang().getInvites().add(invitie);
                    invitie.sendMessage(ChatColor.DARK_AQUA + "You have been invited to " + member.getGang().getName() + " do /gang join " + member.getGang().getName());
                    return true;
                }
                else if(args[0].toLowerCase().equals("create")){

                    if (!p.hasPermission("gangs.create")){
                        p.sendMessage(ChatColor.AQUA + "You need to be Diamond rank to create a Gang!");
                        return true;
                    }

                    if (args.length < 3){
                        p.sendMessage(ChatColor.AQUA + "/gang create <name> <tag>");
                        return true;
                    }

                    if (main.GetMember(p).getGang() != null){
                        p.sendMessage(ChatColor.AQUA + "You Have to not be in any Gang To Create One!");
                        return true;
                    }

                    List<String> members = new ArrayList<>();
                    members.add(p.getUniqueId().toString());

                    if (args[args.length-1].contains("&")){
                        p.sendMessage(ChatColor.AQUA + "You cant put color Codes in the Tag Yet");
                        return true;
                    }else if(args[args.length-1].length() > 3){
                        p.sendMessage(ChatColor.AQUA + "Your Tag Cant be more than 3 Characters long");
                        return true;
                    }

                    if (main.GetGangByTag(args[args.length-1]) != null){
                        p.sendMessage(ChatColor.AQUA + "That Gang Tag is already in use");
                        return true;
                    }

                    List<String> name = new ArrayList<>();
                    for (String arg:args){
                        name.add(arg);
                    }

                    name.remove(0);
                    name.remove(args.length-2);

                    if (main.GetGangByName(String.join(" ",name)) != null){
                        p.sendMessage(ChatColor.AQUA + "That Gang Name is already in use");
                        return true;
                    }

                    Gang gang = new Gang(String.join(" ",name),args[args.length-1],0, new HashMap<String, Integer>(),members,p.getUniqueId().toString(), new HashMap<String, Integer>(), new HashMap<String, Integer>());
                    main.getGangs().add(gang);
                    main.Save();
                    p.sendMessage(ChatColor.DARK_AQUA + "You have successfully created the gang " + ChatColor.AQUA + gang.getName());
                    Member member = main.GetMember(p);
                    member.setGang(gang);
                    main.SaveMember(member);
                    return true;

                }
                else if(args[0].toLowerCase().equals("kick")){
                    Member member = main.GetMember(p);

                    if (member.getGang() == null){
                        p.sendMessage(ChatColor.AQUA + "You need to be the Leader of a Gang to kick People");
                        return true;
                    }else if (!member.getGang().getOwner().equals(p.getUniqueId().toString())){
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
                else if(args[0].toLowerCase().equals("info")){
                    Member member = main.GetMember(p);
                    if (args.length == 1){
                        if (member.getGang() != null){
                            if (args.length < 2) {
                                Gang gang = member.getGang();
                                List<String> mesage = new ArrayList<>();
                                mesage.add(ChatColor.DARK_AQUA + "Gang name:" + ChatColor.AQUA + gang.getName());
                                mesage.add(ChatColor.DARK_AQUA + "Tag:" + ChatColor.AQUA + gang.getTag());
                                mesage.add(ChatColor.DARK_AQUA + "Members:" + ChatColor.AQUA + gang.getMembers().size());
                                mesage.add(ChatColor.DARK_AQUA + "Total WS:"+ChatColor.AQUA +gang.GetTotalWS());
                                mesage.add(ChatColor.DARK_AQUA + "Total Blocks Mined:"+ChatColor.AQUA +gang.GetTotalBlocks());
                                mesage.add(ChatColor.DARK_AQUA + "Go /gang info [name] - to See Other Gangs info");
                                p.sendMessage(mesage.toArray(new String[mesage.size()]));
                            }else{
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
                                mesage.add(ChatColor.DARK_AQUA + "Go /gang info [name] - to See Other Gangs info");
                                p.sendMessage(mesage.toArray(new String[mesage.size()]));
                            }
                        }else{
                            p.sendMessage(ChatColor.DARK_AQUA + "Usage: /gang info [name]");

                            if (args.length < 2){
                                p.sendMessage(ChatColor.AQUA +"Invalid Format");
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

                            List<String> mesage = new ArrayList<>();
                            mesage.add(ChatColor.DARK_AQUA + "Gang name:"+ChatColor.AQUA+gang.getName());
                            mesage.add(ChatColor.DARK_AQUA + "Tag:"+ChatColor.AQUA+gang.getTag());
                            mesage.add(ChatColor.DARK_AQUA + "Members:"+ChatColor.AQUA+gang.getMembers().size());
                            mesage.add(ChatColor.DARK_AQUA + "Total WS:"+ChatColor.AQUA +gang.GetTotalWS());
                            mesage.add(ChatColor.DARK_AQUA + "Total Blocks Mined:"+ChatColor.AQUA +gang.GetTotalBlocks());
                            mesage.add(ChatColor.DARK_AQUA + "Go /gang info [name] - to See Other Gangs info");
                            p.sendMessage(mesage.toArray(new String[mesage.size()]));
                        }
                    }
                }
            }else{
                if (main.GetMember(p).getGang() != null) {
                    if (main.GetMember(p).getGang().getOwner().equals(p.getUniqueId().toString())) {
                        MainMenu.Open(main,p);
                    }
                }
            }


        }

        return true;
    }
}
