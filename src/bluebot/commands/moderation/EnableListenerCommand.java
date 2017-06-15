package bluebot.commands.moderation;

import bluebot.MainBot;
import bluebot.utils.Command;
import net.dv8tion.jda.Permission;
import net.dv8tion.jda.events.message.MessageReceivedEvent;
import net.dv8tion.jda.utils.PermissionUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @file EnableListenerCommand.java
 * @author Blue
 * @version 0.1
 * @brief Enable functionalities of BlueBot on a server.
 */
public class EnableListenerCommand implements Command {

    private static final Map<String, ArrayList<String>> link;
    static
    {
        link = new HashMap<>();
        link.put("twitch", MainBot.getTwitchDisabled());
        link.put("bw", MainBot.getBwDisabled());
        link.put("cleverbot", MainBot.getCleverbotDisabled());
        link.put("userevent", MainBot.getUserEventDisabled());
    }

    private final String HELP = "The command `enable` enable the specified BlueBot functionality." +
            "\nThis command requires the manage messages permission." +
            "\n\nUsage :" +
            "\n\n`!enable twitch` : enable Twitch notifications" +
            "\n`!enable bw` : enable the bad words filter" +
            "\n`!enable cleverbot` : enable CleverBot integration (currently not active)" +
            "\n`!enable userevent` : enable role auto-assign and welcome/goodbye messages";

    private boolean permissionFail = false;

    @Override
    public boolean called(String[] args, MessageReceivedEvent event) {
        if(args.length == 0 || args[0].equals("help")) {return false;}
        else {
            if(PermissionUtil.checkPermission(event.getGuild(), event.getAuthor(), Permission.MESSAGE_MANAGE)) {
                return true;
            } else {
                event.getTextChannel().sendMessage(event.getAuthor().getAsMention() + ", you don't have the permission to do that.");
                permissionFail = true;
                return false;
            }
        }
    }

    @Override
    public void action(String[] args, MessageReceivedEvent event) {
        if (link.containsKey(args[0])) {
            if(link.get(args[0]).contains(event.getGuild().getId())) {
                link.get(args[0]).remove(event.getGuild().getId());
                event.getTextChannel().sendMessage("The functionality has been enabled.");
            } else {
                event.getTextChannel().sendMessage("This functionality is already enabled.");
            }
        } else {
            event.getTextChannel().sendMessage("Invalid entry, please try again.");
        }

    }

    @Override
    public String help() {
        return HELP;
    }

    @Override
    public void executed(boolean success, MessageReceivedEvent event) {
        if (!success) {
            if(!permissionFail) {
                event.getTextChannel().sendMessage(help());
            }
            permissionFail = false;
        }
    }
}
