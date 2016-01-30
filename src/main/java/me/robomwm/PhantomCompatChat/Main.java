package me.robomwm.PhantomCompatChat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

/**
 * Created by Robo on 12/17/2015.
 */
public class Main extends JavaPlugin
{
    @Override
    public void onEnable()
    {
        //no listeners to register :)
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args)
    {
        if (cmd.getName().equalsIgnoreCase("tell"))
        {
            //If message cannot be sent due to lack of arguments...
            if (args.length < 2)
                return false;
            Player target = Bukkit.getPlayerExact(args[0]); //replicating vanilla /tell

            boolean isConsole = true;
            Player player = null;

            if (sender instanceof Player)
            {
                player = (Player)sender;
                isConsole = false;
            }

            //check if player is trying to send a message to themselves
            if (player == target)
            {
                cannotSendToSelf(sender, isConsole);
                return true;
            }

            //check if target exists...
            if (target == null)
            {
                cannotFind(sender, isConsole);
                return true;
            }

            //check if sender has permission to see the target player
            if (!isConsole)
            {
                if (!player.canSee(target))
                {
                    //if not, send "cannot find" message
                    cannotFind(player, false);
                }
            }

            //Prepare message to be sent
            String pm = ":";
            for (int i = 1; i < args.length; i++)
                pm += (" " + args[i]);

            //Send message to target
            if (isConsole) //Bukkit uses "CONSOLE" instead of "Server" as the console's name :/
                target.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "Server whispers to you" + pm);
            else
                target.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + sender.getName() + " whispers to you" + pm);
            //Send feedback to sender
            sender.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You whisper to " + target.getName() + pm);
            return true;
        }
        return true; //Should not be reached
    }

    public void cannotSendToSelf(CommandSender sender, Boolean isConsole)
    {
        this.getLogger().log(Level.INFO, "[" + sender.getName() + ": You can't send a private message to yourself!]");
        if (!isConsole)
            sender.sendMessage(ChatColor.RED + "You can't send a private message to yourself!");
    }

    public void cannotFind(CommandSender sender, Boolean isConsole)
    {
        this.getLogger().log(Level.INFO, "[" + sender.getName() + ": That player cannot be found]");
        if (!isConsole)
            sender.sendMessage(ChatColor.RED + "That player cannot be found");
    }
}