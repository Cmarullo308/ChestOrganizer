package me.ChestOrganizer.main;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Sorter {
	ChestOrganizer plugin;

	public Sorter(ChestOrganizer plugin) {
		this.plugin = plugin;
	}

	public void sortContainer(Block blockClicked, Player player) {
		Container container = (Container) blockClicked.getState();

		// Sorts the contents but doesn't combine them
		ItemStack[] sortedContents = sortContents(container.getInventory().getContents());

		container.getInventory().clear();

		for (int slot = 0; slot < sortedContents.length; slot++) {
			if (sortedContents[slot] != null) {
				container.getInventory().addItem(sortedContents[slot]);
			}
		}
	}

	public void sortPlayersEnderChest(Player player) {
		ItemStack[] sortedContents = sortContents(player.getEnderChest().getContents());
		sortContents(sortedContents);
		player.getEnderChest().clear();
		for (int slot = 0; slot < player.getEnderChest().getSize(); slot++) {
			if (sortedContents[slot] != null) {
				player.getEnderChest().addItem(sortedContents[slot]);
			}
		}
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

		//Compare type
		int result = itemStack1.getType().compareTo(itemStack2.getType());

		if (result != 0) {
			return result;
		}
		
		// Compare metas
		result = itemStack1.getItemMeta().toString().compareTo(itemStack2.getItemMeta().toString());

		return result;
	}

	public void sortPlayerInventory(Player player) {
		ItemStack[] upperitems = new ItemStack[27];

		// Add upper items to upperItems[]
		for (int i = 9; i < 36; i++) {
			if (player.getInventory().getContents()[i] != null) {
				upperitems[i - 9] = player.getInventory().getContents()[i];
			}
		}

		// Sort
		sortContents(upperitems);

		ItemStack[] barItems = new ItemStack[9];
		for (int i = 0; i < 9; i++) {
			if (player.getInventory().getContents()[i] != null) {
				barItems[i] = player.getInventory().getContents()[i];
			}
		}

		ItemStack[] armorItems = new ItemStack[5]; // 36
		for (int i = 0; i < 5; i++) {
			if (player.getInventory().getContents()[i + 36] != null) {
				armorItems[i] = player.getInventory().getContents()[i + 36];
			}
		}

		player.getInventory().clear();

		// Make all the bottom slots water bukkets temporarily for when it adds the rest
		for (int i = 0; i < 9; i++) {
			player.getInventory().addItem(new ItemStack(Material.WATER_BUCKET));
		}

		//Adds all the upperitems back
		for (int i = 0; i < upperitems.length; i++) {
			if (upperitems[i] != null) {
				player.getInventory().addItem(upperitems[i]);
			}
		}

		//Adds all the armor back and left hand
		if (armorItems[0] != null) {
			player.getInventory().setBoots(armorItems[0]);
		}
		if (armorItems[1] != null) {
			player.getInventory().setLeggings(armorItems[1]);
		}
		if (armorItems[2] != null) {
			player.getInventory().setChestplate(armorItems[2]);
		}
		if (armorItems[3] != null) {
			player.getInventory().setHelmet(armorItems[3]);
		}
		if (armorItems[4] != null) {
			player.getInventory().setItemInOffHand(armorItems[4]);
		}
		//--------------------------------------

		player.updateInventory();

		ItemStack[] newItems = player.getInventory().getContents();

		for (int i = 0; i < 9; i++) {
			if (barItems[i] != null) {
				newItems[i] = barItems[i];
			} else {
				newItems[i] = null;
			}
		}

		player.getInventory().setContents(newItems);
		player.updateInventory();

	}
}
