package me.ChestOrganizer.main;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ChestOrganizer extends JavaPlugin implements CommandExecutor {
	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(new MyEvents(this), this);
		super.onEnable();
	}

	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		sender.sendMessage("Test");

		return true;
	}

	public void onDisable() {

	}
}
