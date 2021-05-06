package fr.kolowy.myplugin.commands;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSpawn implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String msg, String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player)sender;
			Location spawn = new Location(player.getWorld(), -213, 96, 229, 1.8f, 7.4f);
			player.sendMessage("Vous vennez d'etre tp au spawn !");
			player.teleport(spawn);
		}
		
		return false;
	}

}
