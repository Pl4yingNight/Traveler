package net.runeduniverse.mc.plugins.traveler.listener;

import org.bukkit.event.Listener;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;

public class ActionListener implements Listener {

	private TravelerMain plugin;
	
    public ActionListener(TravelerMain plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }
}