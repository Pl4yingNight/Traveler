package net.runeduniverse.mc.plugins.traveler;

import org.bukkit.Material;
import org.bukkit.inventory.ShapedRecipe;

public class ItemHub {
	public static ShapedRecipe getLocationTokenRecipe() {
		ShapedRecipe item = new ShapedRecipe(null, null);
		item.shape("---", "---", "--");
		item.setIngredient('N', Material.NAME_TAG);
		item.setIngredient('I', Material.IRON_INGOT);
		item.setIngredient('P', Material.PAPER);
		return (item);
	}
}
