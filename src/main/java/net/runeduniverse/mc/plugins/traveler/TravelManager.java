package net.runeduniverse.mc.plugins.traveler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.plugin.Plugin;

import net.runeduniverse.mc.plugins.traveler.model.Traveler;

public class TravelManager {

	private static final String PUBLIC_TRAVELERS_QUERY = "";

	@Deprecated
	public List<Traveler> publicTravelers = new ArrayList<Traveler>();
	
	private Plugin plugin;
	private int task = -1;

	public TravelManager(Plugin plugin) {
		this.plugin = plugin;
		this.task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, new PublicTravelerUpdater(this), 0, 12000);
	}
	
	public void disable() {
		this.plugin.getServer().getScheduler().cancelTask(this.task);
	}

	public void addNPC(Traveler traveler) {
		TravelerMain.travelerSession.save(traveler);
	}

	class PublicTravelerUpdater implements Runnable {

		TravelManager manager;

		public PublicTravelerUpdater(TravelManager manager) {
			this.manager = manager;
		}

		@Override
		public void run() {
			this.manager.publicTravelers = (List<Traveler>) TravelerMain.travelerSession.query(Traveler.class,
					PUBLIC_TRAVELERS_QUERY, new HashMap<String, Object>());
		}
	}
}
