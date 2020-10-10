package net.runeduniverse.mc.plugins.traveler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import net.runeduniverse.mc.plugins.snowflake.api.Snowflake;
import net.runeduniverse.mc.plugins.snowflake.api.exceptions.InconsistentException;
import net.runeduniverse.mc.plugins.snowflake.api.exceptions.SnowflakeNotFoundException;
import net.runeduniverse.mc.plugins.traveler.data.AdventurerData;
import net.runeduniverse.mc.plugins.traveler.listener.ActionListener;

@Plugin(name = "Traveler", version = "1.0.1")
@Description(value = "This Plugin makes fast traveling possible")
@Author(value = "Pl4yingNight")
@Dependency("Snowflake")
@ApiVersion(Target.v1_13)
public class TravelerMain extends JavaPlugin{
	
	private Snowflake snowflake = null;
	private TravelManager travelManager = null;
	
	@Override
	public void onLoad() {
		try {
			this.snowflake = Snowflake.extract(this);
		} catch (SnowflakeNotFoundException | InconsistentException e) {
			e.printStackTrace();
		}
		
		
		snowflake.registerNodePackage("net.runeduniverse.mc.plugins.traveler.model");
		snowflake.registerPlayerData(AdventurerData.class);
		
		
		getLogger().info("Loaded");
	}
	
	@Override
	public void onEnable() {
		this.travelManager = new TravelManager(this);
		new ActionListener(this);
		
		snowflake.getRecipeService().registerRecipe(ItemHub.LOCATION_TOKEN, ItemHub.getLocationTokenRecipe());
		
		getLogger().info("Enabled");
	}
	
	@Override
	public void onDisable() {
		this.travelManager.disable();
		getLogger().info("Disabled");
	}
}