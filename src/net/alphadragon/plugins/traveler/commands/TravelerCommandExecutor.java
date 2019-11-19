package net.alphadragon.plugins.traveler.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.annotation.command.*;

import net.alphadragon.plugins.traveler.Main;

@Commands(@Command(name = "get_traveler", usage = "/get_traveler"))
public class TravelerCommandExecutor implements CommandExecutor {

	public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
		if(!sender.isOp())
			return true;
		if(!(sender instanceof Player))
			return true;
		
		((Player)sender).getInventory().addItem(Main.getTravelerEgg());
		return true;
	}

}
