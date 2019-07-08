package me.ChestOrganizer.main;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestOrganizer extends JavaPlugin implements CommandExecutor {
	Sorter sorter = new Sorter(this);

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new MyEvents(this), this);
		super.onEnable();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args[0].equalsIgnoreCase("SortPlayerInventory") || args[0].equalsIgnoreCase("spi")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage(ChatColor.RED + "Must be a player to run this command");
				return true;
			}

			Player player = (Player) sender;

			if (!player.hasPermission("ChestOrganizer.SortPlayerInventory")) {
				noPermissionMessage(player);
			}

			sorter.sortPlayerInventory(player);
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
