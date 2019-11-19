package net.runeduniverse.mc.plugins.traveler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import net.runeduniverse.mc.plugins.traveler.commands.TravelerCommandExecutor;
import net.runeduniverse.mc.plugins.traveler.listener.ActionListener;

@Plugin(name = "Traveler", version = "0.0.1")
@Description(value = "This Plugin makes fast traveling possible")
@Author(value = "Pl4yingNight")
@ApiVersion(Target.v1_13)
public class Main extends JavaPlugin{
	
	private static Main plugin;
	
	@Override
	public void onLoad() {
		plugin = this;
		
		getLogger().info("Loaded");
	}
	
	@Override
	public void onEnable() {
		
		new ActionListener(this);
		
		//getServer().addRecipe(getTravelerEggRecipe());
		
		getLogger().info("Enabled");
	}
	
	@Override
	public void onDisable() {
		
		getLogger().info("Disabled");
	}
	
	public static org.bukkit.plugin.Plugin getPlugin() {
		return plugin;
	}
	
	public PlayerMap loadPlayer(UUID uuid) {
		if(data.keySet().contains(uuid))
			return data.get(uuid);
		
		PlayerMap map = PlayerMap.load(uuid); 
		data.put(uuid, map);
		return map;
	}
	
	public void savePlayer(UUID uuid) {
		loadPlayer(uuid).save();
		data.remove(uuid);
	}
	
	/*public static org.bukkit.inventory.ShapedRecipe getTravelerEggRecipe(){
		org.bukkit.inventory.ShapedRecipe item = new org.bukkit.inventory.ShapedRecipe(new NamespacedKey(plugin, "traveler_egg"), getTravelerEgg());
		item.shape("MMM","MEM","MMM");   //X = nix
		item.setIngredient('M', Material.PHANTOM_MEMBRANE);
		item.setIngredient('E', Material.EMERALD_BLOCK);
		return(item);
	}*/
}