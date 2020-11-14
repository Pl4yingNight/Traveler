package net.runeduniverse.mc.plugins.traveler.data;

import org.bukkit.NamespacedKey;

public interface NamespacedKeys {

	// Journal <ItemStack>

	@SuppressWarnings("deprecation")
	public static final NamespacedKey JOURNAL_KEY = new NamespacedKey("traveler", "journal");
	@SuppressWarnings("deprecation")
	public static final NamespacedKey JOURNAL_TRAVELER_ID_KEY = new NamespacedKey("traveler", "journal_traveler_id");

	// TravelerService <ItemStack>

	@SuppressWarnings("deprecation")
	public static final NamespacedKey TOKEN_KEY = new NamespacedKey("traveler", "travel_token");

	// Traveler <Entity>

	@SuppressWarnings("deprecation")
	public static final NamespacedKey TRAVELER_ID_KEY = new NamespacedKey("traveler", "traveler_id");
}
