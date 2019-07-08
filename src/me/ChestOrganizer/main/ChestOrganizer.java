package me.ChestOrganizer.main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestOrganizer extends JavaPlugin implements CommandExecutor {
	Sorter sorter = new Sorter(this);
	String helpMessage = "Click on a container with nothing in your hand to sort that container";

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new MyEvents(this), this);
		super.onEnable();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 1) {
			if (args[0].equalsIgnoreCase("SortPlayerInventory") || args[0].equalsIgnoreCase("spi")) {
				if (!(sender instanceof Player)) {
					sender.sendMessage(ChatColor.RED + "Must be a player to run this command");
					return true;
				}
				Player player = (Player) sender;

				if (!player.hasPermission("ChestOrganizer.SortPlayerInventory") && !player.isOp()) {
					noPermissionMessage(player);
					return true;
				}

				sorter.sortPlayerInventory(player);
			} else if (args[0].equalsIgnoreCase("help")) {
				
			}
		} else if (args.length == 2) {
			if (args[0].equalsIgnoreCase("SortPlayerInventory") || args[0].equalsIgnoreCase("spi")) {
				Player player = (Player) sender;

				if (!player.hasPermission("ChestOrganizer.SortPlayerInventory.Other") && !player.isOp()) {
					noPermissionMessage(player);
					return true;
				}

				Player targetPlayer = getServer().getPlayer(args[1]);

				if (player != null && player.isOnline()) {
					sorter.sortPlayerInventory(targetPlayer);
				} else {
					sender.sendMessage(ChatColor.RED + args[1] + " is not in the server");
				}
			}
		}

		return true;
	}

	public void consoleMessage(String message) {
		getLogger().info(message);
	}

	private void noPermissionMessage(Player player) {
		player.sendMessage(ChatColor.RED + "You do not have permissin to run this command");
	}

	public void onDisable() {

	}
}
