package net.runeduniverse.mc.plugins.traveler.data.items;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import net.runeduniverse.mc.plugins.snowflake.api.data.model.ItemData;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.data.NamespacedKeys;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;

public class Journal implements NamespacedKeys {

	public static final ItemStack BLANK_JOURNAL;
	public static final ShapedRecipe JOURNAL_RECIPE;

	static {
		// CONFIGURE JOURNAL_RECIPE
		BLANK_JOURNAL = new ItemStack(Material.WRITTEN_BOOK);

		BookMeta meta = (BookMeta) BLANK_JOURNAL.getItemMeta();
		meta.setTitle("Journal");
		meta.setLore(Arrays.asList("A Journal can be used to turn", "a Wandering Trader into a Traveler",
				"and/or configure a Traveler"));
		BLANK_JOURNAL.setItemMeta(meta);

		/*
		 * BookMeta bookMeta = (BookMeta) BLANK_JOURNAL.getItemMeta();
		 * 
		 * // create a page BaseComponent[] page = new ComponentBuilder("Click me")
		 * .event(new ClickEvent(ClickEvent.Action.OPEN_URL, "http://spigotmc.org"))
		 * .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new
		 * ComponentBuilder("Go to the spigot website!").create())) .create();
		 * 
		 * // add the page to the meta bookMeta.spigot().addPage(page);
		 * 
		 * // set the title and author of this book
		 * bookMeta.setTitle("Interactive Book"); bookMeta.setAuthor("gigosaurus");
		 * 
		 * // update the ItemStack with this new meta
		 * BLANK_JOURNAL.setItemMeta(bookMeta);
		 */

		// CONFIGURE JOURNAL_RECIPE
		JOURNAL_RECIPE = new ShapedRecipe(JOURNAL_KEY, BLANK_JOURNAL);
		JOURNAL_RECIPE.shape("-L-", "FBI", "-M-");
		JOURNAL_RECIPE.setIngredient('B', Material.BOOK);
		JOURNAL_RECIPE.setIngredient('F', Material.FEATHER);
		JOURNAL_RECIPE.setIngredient('I', Material.INK_SAC);
		JOURNAL_RECIPE.setIngredient('M', Material.MAP);
		JOURNAL_RECIPE.setIngredient('L', Material.LANTERN);
	} // 19 x char per line | 14 x lines per page

	private static TravelerMain main;

	public static void inject(TravelerMain main) {
		Journal.main = main;
	}

	public static Result setName(ItemStack journal, String name) {
		if (!isJournal(journal))
			return Result.NOT_A_JOURNAL;
		// TODO set NPCs name
		return Result.SUCCESS;
	}

	public static Result setDestinationName(ItemStack journal, String name) {
		if (!isJournal(journal))
			return Result.NOT_A_JOURNAL;
		Traveler traveler = getTraveler(journal);
		if (traveler == null)
			return Result.NO_TRAVELER_SELECTED;
		traveler.setName(name);
		TravelerService.INSTANCE.saveTraveler(traveler);
		return Result.SUCCESS;
	}

	public static Result setLocation(ItemStack journal, Location location) {
		if (!isJournal(journal))
			return Result.NOT_A_JOURNAL;
		Traveler traveler = getTraveler(journal);
		if (traveler == null)
			return Result.NO_TRAVELER_SELECTED;
		traveler.setHome(location);
		TravelerService.INSTANCE.saveTraveler(traveler);
		return Result.SUCCESS;
	}

	public static Result setDestinationLocation(ItemStack journal, Location location) {
		if (!isJournal(journal))
			return Result.NOT_A_JOURNAL;
		Traveler traveler = getTraveler(journal);
		if (traveler == null)
			return Result.NO_TRAVELER_SELECTED;
		traveler.setLocation(location);
		TravelerService.INSTANCE.saveTraveler(traveler);
		return Result.SUCCESS;
	}

	private static boolean isJournal(ItemStack journal) {
		String key = journal.getItemMeta().getPersistentDataContainer().get(ItemData.ITEM_DATA_KEY,
				PersistentDataType.STRING);
		if (key == null)
			return false;
		return JOURNAL_KEY.toString().equals(key);
	}

	private static Traveler getTraveler(ItemStack journal) {
		Long id = journal.getItemMeta().getPersistentDataContainer().get(JOURNAL_TRAVELER_ID_KEY,
				PersistentDataType.LONG);
		if (id == null)
			return null;
		return TravelerService.INSTANCE.loadTraveler(id);
	}

	public static enum Result {
		SUCCESS, NOT_A_JOURNAL, NO_TRAVELER_SELECTED
	}
}
