package net.runeduniverse.mc.plugins.traveler.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.metadata.MetadataValueAdapter;
import org.bukkit.persistence.PersistentDataType;

import net.md_5.bungee.api.ChatColor;
import net.runeduniverse.mc.plugins.traveler.Main;
import net.runeduniverse.mc.plugins.traveler.PlayerMap;

public class ActionListener implements Listener {

	private Main plugin;
	
    public ActionListener(Main plugin) {
    	this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEntityEvent event) {
    	if(!event.getRightClicked().getCustomName().startsWith(ChatColor.DARK_GREEN+"[TRAVELER]"))
    		return;

		NamespacedKey key = new NamespacedKey(Main.getPlugin(), "id");
    	if(!event.getRightClicked().getPersistentDataContainer().has(key, PersistentDataType.LONG))
			return;
    	
    	event.setCancelled(true);
    	
    	plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				PlayerMap map = plugin.loadPlayer(event.getPlayer().getUniqueId());
				Long id = event.getRightClicked().getPersistentDataContainer().get(key, PersistentDataType.LONG);
				if(map.addPlace(id))
					event.getPlayer().sendMessage(ChatColor.DARK_GREEN+"Traveler in "+Options.getOptions().getMapEntry(id).getName()+" found!");
				event.getPlayer().openInventory(map.getInv(event.getRightClicked().getCustomName()));
			}
		});
    }
    
    @EventHandler
    public void onTravel(InventoryClickEvent event) {
    	if(event.getClickedInventory().getType() != InventoryType.CHEST)
    		return;
    	if(!event.getView().getTitle().startsWith(ChatColor.DARK_GREEN+"[TRAVELER]"))
    		return;
    	if(event.getCurrentItem() == null || event.getCurrentItem().getType()==Material.AIR)
    		return;
    	
    	event.setCancelled(true);
    	
    	plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
			
			@Override
			public void run() {
				NamespacedKey id = new NamespacedKey(Main.getPlugin(), "id");
				
				if(!event.getCurrentItem().getItemMeta().getPersistentDataContainer().has(id, PersistentDataType.LONG))
					return;
				
				LocationToken entry = Options.getOptions().getMapEntry(event.getCurrentItem().getItemMeta().getPersistentDataContainer().get(id, PersistentDataType.LONG));
				Location loc = event.getWhoClicked().getLocation();
				loc.setWorld(plugin.getServer().getWorld(entry.getWorld()));
				loc.setX(entry.getX());
				loc.setY(entry.getY());
				loc.setZ(entry.getZ());
				
				event.getWhoClicked().teleport(loc);
				event.getWhoClicked().sendMessage(ChatColor.DARK_GREEN+"A traveler brought you to "+entry.getName());
			}
		});
    }
    
    @EventHandler
    public void onTravelerSpawned(EntitySpawnEvent event) {
    	if(event.getEntity().getType() != EntityType.WANDERING_TRADER)
    		return;
    	if(event.getEntity().getCustomName().isEmpty())
    		return;
    	if(event.getEntity().getCustomName().contains("[LOCATION NAME]")) {
    		event.setCancelled(true);
    		return;
    	}
    	
    	Entity entity = event.getEntity();
    	Location location = entity.getLocation();
    	
    	LocationToken entry = new LocationToken().setName(entity.getCustomName()).setWorld(location.getWorld().getName()).setX(location.getX()).setY(location.getY()).setZ(location.getZ());
    	Options.getOptions().addMapEntry(entry);
    	entity.setCustomName(ChatColor.DARK_GREEN+"[TRAVELER] "+entity.getCustomName());
    	entity.getPersistentDataContainer().set(new NamespacedKey(plugin, "id"), PersistentDataType.LONG, entry.getID());
    }
    
    @EventHandler
    public void onTravelerKilled(EntityDeathEvent event) {
    	if(event.getEntityType() != EntityType.WANDERING_TRADER)
    		return;
    	
    	if(!event.getEntity().getCustomName().startsWith(ChatColor.DARK_GREEN+"[TRAVELER]"))
    		return;
    	
    	Options.getOptions().removeMapEntry(event.getEntity().getPersistentDataContainer().get(new NamespacedKey(Main.getPlugin(), "id"), PersistentDataType.LONG));
    }
    
    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
    	plugin.savePlayer(event.getPlayer().getUniqueId());
    }
}