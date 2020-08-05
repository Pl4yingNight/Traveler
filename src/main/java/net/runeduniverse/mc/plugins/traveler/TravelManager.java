package net.runeduniverse.mc.plugins.traveler;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.plugin.Plugin;

import lombok.Getter;
import net.runeduniverse.libs.rogm.Session;
import net.runeduniverse.libs.rogm.querying.IParameterized;
import net.runeduniverse.mc.plugins.traveler.model.Traveler;

public class TravelManager {

	@Getter
	protected List<Traveler> publicTravelers = new ArrayList<Traveler>();

	private Plugin plugin;
	private int task = -1;

	public TravelManager(Plugin plugin) {
		this.plugin = plugin;
		try {
			this.task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin,
					new PublicTravelerUpdater(this), 0, 12000);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disable() {
		this.plugin.getServer().getScheduler().cancelTask(this.task);
	}

	public void addNPC(Traveler traveler) {
		TravelerMain.travelerSession.save(traveler);
	}

	private static class PublicTravelerUpdater implements Runnable {

		private final TravelManager manager;
		private final IParameterized filter;
		private final Session session;

		@SuppressWarnings("deprecation")
		private PublicTravelerUpdater(TravelManager manager) throws Exception {
			this.manager = manager;
			this.session = TravelerMain.travelerSession;
			this.filter = (IParameterized) this.session.getPattern(Traveler.class).search(true);
			filter.getParams().put("publiclyAccessible", true);
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			this.manager.publicTravelers = (List<Traveler>) session.loadAll(Traveler.class, filter);
			session.resolveAllLazyLoaded(this.manager.publicTravelers, 3);
		}
	}
}
