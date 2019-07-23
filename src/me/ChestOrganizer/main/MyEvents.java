package me.ChestOrganizer.main;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class MyEvents implements Listener {
	ChestOrganizer plugin;

	public MyEvents(ChestOrganizer plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onPlayerInteractEvent(PlayerInteractEvent e) {
		if (e.getAction() != Action.LEFT_CLICK_BLOCK) {
			return;
		}

		Player player = e.getPlayer();

		if (player.getGameMode() != GameMode.SURVIVAL && player.getGameMode() != GameMode.ADVENTURE) {
			return;
		}

		if (!player.hasPermission("ChestOrganizer.SortContainer")) {
			return;
		}

		if (!isPlayerHandEmpty(player)) {
			return;
		}

		Block blockClicked = player.getTargetBlockExact(5);

		if (blockClicked.getType() == Material.ENDER_CHEST) {
			plugin.sorter.sortPlayersEnderChest(player);
			player.sendMessage(ChatColor.AQUA + "Your ender chest is sorted!");
			return;
		} else if (isSortableContainer(blockClicked)) {
			plugin.sorter.sortContainer(blockClicked, player);
			player.sendMessage(ChatColor.AQUA + "Your " + blockTypeWithoutUnderscores(blockClicked) + " is sorted!");
			return;
		} else {
			return;
		}
	}

	private String blockTypeWithoutUnderscores(Block blockClicked) {
		if (isDyedShulkerBox(blockClicked)) {
			String[] containerName = blockClicked.getType().toString().replace('_', ' ').toLowerCase().split(" ");
			return containerName[1] + " " + containerName[2];

		} else {
			return blockClicked.getType().toString().replace('_', ' ').toLowerCase();
		}
	}

	private boolean isDyedShulkerBox(Block blockClicked) {
		switch (blockClicked.getType()) {
		case WHITE_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case BLACK_SHULKER_BOX:
			return true;
		default:
			return false;
		}
	}

	private boolean isSortableContainer(Block blockClicked) {
		switch (blockClicked.getType()) {
		case CHEST:
		case SHULKER_BOX:
		case WHITE_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case BLACK_SHULKER_BOX:
		case BARREL:
		case HOPPER:
			return true;
		default:
			return false;
		}
	}

	private boolean isPlayerHandEmpty(Player player) {
		return player.getInventory().getContents()[player.getInventory().getHeldItemSlot()] == null;
	}
}
