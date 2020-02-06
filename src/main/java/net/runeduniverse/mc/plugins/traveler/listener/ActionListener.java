package net.runeduniverse.mc.plugins.traveler.listener;

import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import net.runeduniverse.mc.plugins.traveler.ItemHub;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;

public class ActionListener implements Listener {

	private TravelerMain plugin;

	public ActionListener(TravelerMain plugin) {
		this.plugin = plugin;
		plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	public void locTokenUse(PlayerInteractEvent event) {
		ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
    	if(!ItemHub.isItem(item, ItemHub.LOCATION_TOKEN))
    		return;
    	
    	if(!event.getPlayer().isSneaking()||(!(event.getAction().equals(Action.RIGHT_CLICK_AIR)||event.getAction().equals(Action.RIGHT_CLICK_BLOCK))))
    		return;
    	
    	event.setCancelled(true);
    	event.getPlayer().sendMessage("Location saved!");
    	ItemHub.setLocationToken(plugin, item, event.getPlayer().getLocation());
    }
}