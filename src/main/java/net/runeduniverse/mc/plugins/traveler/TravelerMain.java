package net.runeduniverse.mc.plugins.traveler;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion;
import org.bukkit.plugin.java.annotation.plugin.ApiVersion.Target;
import org.bukkit.plugin.java.annotation.plugin.Description;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.author.Author;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

import lombok.Getter;
import net.runeduniverse.mc.plugins.snowflake.api.Snowflake;
import net.runeduniverse.mc.plugins.snowflake.api.exceptions.InconsistentException;
import net.runeduniverse.mc.plugins.snowflake.api.exceptions.SnowflakeNotFoundException;
import net.runeduniverse.mc.plugins.traveler.commands.TravelerCommandExecutor;
import net.runeduniverse.mc.plugins.traveler.data.AdventurerData;
import net.runeduniverse.mc.plugins.traveler.data.items.Journal;
import net.runeduniverse.mc.plugins.traveler.listener.ActionListener;
import net.runeduniverse.mc.plugins.traveler.listener.RequestTravelPacketListener;
import net.runeduniverse.mc.plugins.traveler.services.AdventureService;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;

@Plugin(name = "Traveler", version = "1.0.1")
@Description(value = "This Plugin makes fast traveling possible")
@Author(value = "Pl4yingNight")
@Dependency("Snowflake")
@Dependency("ProtocolLib")
@ApiVersion(Target.v1_13)
public class TravelerMain extends JavaPlugin {

	private Snowflake snowflake = null;
	private ProtocolManager protocolManager;

	@Getter
	private AdventureService adventureService = null;
	@Getter
	private TravelerService travelerService = null;

	@Override
	public void onLoad() {
		boolean startable = true;
		try {
			this.snowflake = Snowflake.extract(this);
		} catch (SnowflakeNotFoundException | InconsistentException e) {
			e.printStackTrace();
			startable = false;
		}
		try {
			this.protocolManager = ProtocolLibrary.getProtocolManager();
		} catch (Exception e) {
			e.printStackTrace();
			startable = false;
		}

		if (!startable) {
			this.getPluginLoader().disablePlugin(this);
			this.getLogger()
					.warning("Disabling due to missing dependencies! Please check the log for further information!");
		}

		snowflake.registerNodePackages(this.getClassLoader(), "net.runeduniverse.mc.plugins.traveler.data.model");
		snowflake.registerPlayerData(AdventurerData.class);

		this.adventureService = new AdventureService(this.snowflake, this);
		this.travelerService = new TravelerService(this.snowflake, this);

		getLogger().info("Loaded");
	}

	@Override
	public void onEnable() {
		this.travelerService.inject(this.snowflake.getStorageService().getNeo4jModule());
		this.adventureService.inject(this.snowflake.getPlayerService().getNeo4jModule());
		Journal.inject(this);

		new ActionListener(this);
		this.protocolManager.addPacketListener(new RequestTravelPacketListener(this));

		this.adventureService.prepare();
		this.travelerService.prepare();

		this.getCommand("tnpc").setExecutor(new TravelerCommandExecutor(this.travelerService,
				this.snowflake.getStorageService(), this.snowflake.getPlayerService()));

		this.snowflake.getRecipeService().registerRecipe(Journal.JOURNAL_RECIPE);

		getLogger().info("Enabled");
	}

	@Override
	public void onDisable() {
		getLogger().info("Disabled");
	}
}