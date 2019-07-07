package me.ChestOrganizer.main;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

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

		if (!player.hasPermission("ChestOrganizer.SortContainer")) {
			player.sendMessage("NP");
			return;
		}

		if (!isPlayerHandEmpty(player)) {
			return;
		}

		Block blockClicked = player.getTargetBlockExact(5);

		if (blockClicked.getType() == Material.ENDER_CHEST) {
			sortPlayersEnderChest(player);
			return;
		} else if (isSortableContainer(blockClicked)) {
			sortContainer(blockClicked, player);
			return;
		} else {
			return;
		}

//		if (!isSortableContainer(blockClicked)) {
//			return;
//		} else if (blockClicked.getType() == Material.ENDER_CHEST) {
//			sortPlayersEnderChest(player);
//			return;
//		}

	}

	private void sortContainer(Block blockClicked, Player player) {
		Container container = (Container) blockClicked.getState();

		// Sorts the contents but doesn't combine them
		ItemStack[] sortedContents = sortContents(container.getInventory().getContents());

		container.getInventory().clear();

		for (int slot = 0; slot < sortedContents.length; slot++) {
			if (sortedContents[slot] != null) {
				container.getInventory().addItem(sortedContents[slot]);
			}
		}

		player.sendMessage(ChatColor.AQUA + "Your " + typeWithoutUnderscores(blockClicked) + " is sorted!");
	}

	private String typeWithoutUnderscores(Block blockClicked) {
		return blockClicked.getType().toString().replace('_', ' ').toLowerCase();
	}

	private void sortPlayersEnderChest(Player player) {
		ItemStack[] sortedContents = sortContents(player.getEnderChest().getContents());
		sortContents(sortedContents);
		player.getEnderChest().clear();
		for (int slot = 0; slot < player.getEnderChest().getSize(); slot++) {
			if (sortedContents[slot] != null) {
				player.getEnderChest().addItem(sortedContents[slot]);
			}
		}

		player.sendMessage(ChatColor.AQUA + "Your ender chest is sorted!");
	}

	private ItemStack[] sortContents(ItemStack[] contents) {
		quickSortContents(contents, 0, contents.length - 1);

		return contents;
	}

	private void quickSortContents(ItemStack[] contents, int low, int high) {
		if (low < high) {
			int partitionIndex = partition(contents, low, high);

			quickSortContents(contents, low, partitionIndex - 1);
			quickSortContents(contents, partitionIndex + 1, high);
		}

	}

	private int partition(ItemStack[] contents, int low, int high) {
		ItemStack pivot = contents[high];
		int i = low - 1;

		for (int j = low; j < high; j++) {
			if (compareItemStacks(contents[j], pivot) <= 0) {
				i++;

				ItemStack temp = contents[i];
				contents[i] = contents[j];
				contents[j] = temp;
			}
		}

		ItemStack swapTemp = contents[i + 1];
		contents[i + 1] = contents[high];
		contents[high] = swapTemp;

		return i + 1;
	}

	private int compareItemStacks(ItemStack itemStack1, ItemStack itemStack2) {
		if (itemStack1 == null && itemStack2 == null) {
			return 0;
		} else if (itemStack1 == null) {
			return 1;
		} else if (itemStack2 == null) {
			return -1;
		}

		int result = itemStack1.getType().compareTo(itemStack2.getType());

		if (result != 0) {
			return result;
		}

		// Compare metas
		result = itemStack1.getItemMeta().toString().compareTo(itemStack2.getItemMeta().toString());

		return result;
	}

	private boolean isSortableContainer(Block blockClicked) {
		switch (blockClicked.getType()) {
		case CHEST:
			return true;
		case SHULKER_BOX:
			return true;
		case BARREL:
			return true;
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
