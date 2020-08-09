package net.runeduniverse.mc.plugins.traveler;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;

public class ItemHub {

	@SuppressWarnings("deprecation")
	public static final NamespacedKey GLOBAL_NAMESPACED_KEY = new NamespacedKey("global", "namespaced_key");
	@SuppressWarnings("deprecation")
	public static final NamespacedKey LOCATION_TOKEN = new NamespacedKey("traveler", "location_token");
	@SuppressWarnings("deprecation")
	public static final NamespacedKey LOCATION_TOKEN_NBT_POI = new NamespacedKey("traveler", "location_token_nbt_poi");

	
	public static ShapedRecipe getLocationTokenRecipe() {
		ShapedRecipe item = new ShapedRecipe(LOCATION_TOKEN, null);
		item.shape("---", "---", "--");
		item.setIngredient('N', Material.NAME_TAG);
		item.setIngredient('I', Material.IRON_INGOT);
		item.setIngredient('P', Material.PAPER);
		return (item);
	}
}
