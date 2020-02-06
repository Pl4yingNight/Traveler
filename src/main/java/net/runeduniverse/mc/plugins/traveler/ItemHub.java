package net.runeduniverse.mc.plugins.traveler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;

import net.runeduniverse.mc.plugins.traveler.model.PointOfInterest;

public class ItemHub {

	@SuppressWarnings("deprecation")
	public static final NamespacedKey GLOBAL_NAMESPACED_KEY = new NamespacedKey("global", "namespaced_key");
	@SuppressWarnings("deprecation")
	public static final NamespacedKey LOCATION_TOKEN = new NamespacedKey("traveler", "location_token");
	@SuppressWarnings("deprecation")
	public static final NamespacedKey LOCATION_TOKEN_NBT_POI = new NamespacedKey("traveler", "location_token_nbt_poi");

	public static boolean isItem(ItemStack item, NamespacedKey key) {
		ItemMeta meta = item.getItemMeta();
		if(!meta.getPersistentDataContainer().has(GLOBAL_NAMESPACED_KEY, PersistentDataType.STRING))
			return false;
		return meta.getPersistentDataContainer().get(GLOBAL_NAMESPACED_KEY, PersistentDataType.STRING) == key.toString();			
	}

	public static void setLocationToken(Plugin plugin, ItemStack item, Location location) {
		ItemMeta meta = item.getItemMeta();
		List<String> lore = new ArrayList<String>();
		// fill in with poi
		meta.setLore(lore);
		
		plugin.getServer().getScheduler().runTaskAsynchronously(plugin, new Runnable() {
			
			@Override
			public void run() {
				PointOfInterest poi = new PointOfInterest();
				Long poiID = meta.getPersistentDataContainer().get(LOCATION_TOKEN_NBT_POI, PersistentDataType.LONG);
				if(poiID!=-1)
					poi = TravelerMain.travelerSession.load(PointOfInterest.class, poiID, 2);
				// fill poi with features
				TravelerMain.travelerSession.save(poi);
				meta.getPersistentDataContainer().set(LOCATION_TOKEN_NBT_POI, PersistentDataType.LONG, TravelerMain.travelerSession.resolveGraphIdFor(poi));
			}
		});
	}
	
	public static ItemStack getLocationToken() {
		ItemStack item = new ItemStack(Material.NAME_TAG);
		item.setAmount(1);
		
		ItemMeta meta = item.getItemMeta();
		//meta.setCustomModelData(1);
		meta.setDisplayName("Location Token");
		meta.getPersistentDataContainer().set(GLOBAL_NAMESPACED_KEY, PersistentDataType.STRING, LOCATION_TOKEN.toString());
		meta.getPersistentDataContainer().set(LOCATION_TOKEN_NBT_POI, PersistentDataType.LONG, -1l);
		item.setItemMeta(meta);
		return item;
	}

	public static ShapedRecipe getLocationTokenRecipe() {
		ShapedRecipe item = new ShapedRecipe(LOCATION_TOKEN, getLocationToken());
		item.shape("IPI", "PNP", "IPI");
		item.setIngredient('N', Material.NAME_TAG);
		item.setIngredient('I', Material.IRON_INGOT);
		item.setIngredient('P', Material.PAPER);
		return (item);
	}
}
