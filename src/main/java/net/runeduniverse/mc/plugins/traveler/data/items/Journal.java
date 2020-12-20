package net.runeduniverse.mc.plugins.traveler.data.items;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.ItemData;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.data.NamespacedKeys;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;

public class Journal {

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
		 */

		// CONFIGURE JOURNAL_RECIPE
		JOURNAL_RECIPE = new ShapedRecipe(NamespacedKeys.JOURNAL_KEY, BLANK_JOURNAL);
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

	public static void updateTraveler(ItemStack journal, long id) {
		if (!isJournal(journal))
			return;
		journal.getItemMeta().getPersistentDataContainer().set(NamespacedKeys.JOURNAL_TRAVELER_ID_KEY,
				PersistentDataType.LONG, id);
		Journal.update(journal);
	}

	public static void update(ItemStack journal) {
		System.out.println(buildCover(null).toString());
		if (!isJournal(journal))
			return;
		Traveler traveler = getTraveler(journal);
		BookMeta meta = (BookMeta) journal.getItemMeta();
		meta.spigot().addPage(buildCover(traveler));
		if (traveler != null)
			meta.spigot().addPage(buildTravelerPage(traveler), buildDestinationPage(traveler));
		BLANK_JOURNAL.setItemMeta(meta);
	}

	public static boolean isJournal(ItemStack journal) {
		if (journal == null)
			return false;
		// the here checked key gets added my Snowflake once the Recipe gets registered!
		String key = journal.getItemMeta().getPersistentDataContainer().get(ItemData.ITEM_DATA_KEY,
				PersistentDataType.STRING);
		if (key == null)
			return false;
		return NamespacedKeys.JOURNAL_KEY.toString().equals(key);
	}

	private static Traveler getTraveler(ItemStack journal) {
		Long id = journal.getItemMeta().getPersistentDataContainer().get(NamespacedKeys.JOURNAL_TRAVELER_ID_KEY,
				PersistentDataType.LONG);
		if (id == null)
			return null;
		return Journal.main.getTravelerService().loadTraveler(id);
	}

	private static BaseComponent[] buildCover(Traveler traveler) {
		return new ComponentBuilder().append("\n").bold(true).append(" Traveler Journal").reset()
				.append("\n Selected Traveler\n  [Traveler Name]")
				.append("\n    Destination\n [Destination Name]\n [public/private]")
				.append("\n      Owner\n [Player]\n\n").append(" [Traveler Wiki]")
				.event(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://github.com/Pl4yingNight/Traveler/wiki"))
				.create();
	}

	private static BaseComponent[] buildTravelerPage(Traveler traveler) {
		return new ComponentBuilder().create();
	}

	private static BaseComponent[] buildDestinationPage(Traveler traveler) {
		return new ComponentBuilder().create();
	}
}
