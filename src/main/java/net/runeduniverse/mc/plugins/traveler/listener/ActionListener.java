package net.runeduniverse.mc.plugins.traveler.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.persistence.PersistentDataType;

import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.data.AdventurerData;
import net.runeduniverse.mc.plugins.traveler.data.NamespacedKeys;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;
import net.runeduniverse.mc.plugins.traveler.services.AdventureService;
import net.runeduniverse.mc.plugins.traveler.services.TravelerService;

public class ActionListener implements Listener, NamespacedKeys {

	private final TravelerMain plugin;
	private final AdventureService adventureService;
	private final TravelerService travelerService;

	public ActionListener(TravelerMain plugin) {
		this.plugin = plugin;
		this.adventureService = this.plugin.getAdventureService();
		this.travelerService = this.plugin.getTravelerService();
		plugin.getServer().getPluginManager().registerEvents(this, this.plugin);
	}

	@EventHandler
	public void onPlayerInteract(PlayerInteractAtEntityEvent event) {
		Long id = event.getRightClicked().getPersistentDataContainer().get(TRAVELER_ID_KEY, PersistentDataType.LONG);
		if (id == null)
			return;
		event.setCancelled(true);
		plugin.getServer().getScheduler().runTask(plugin, new Runnable() {

			@Override
			public void run() {
				Traveler traveler = ActionListener.this.travelerService.loadTraveler(id);
				AdventurerData data = ActionListener.this.adventureService
						.getAdventurerData(event.getPlayer().getUniqueId());
				if (data.getAdventurer().addTraveler(traveler))
					event.getPlayer().sendMessage("Destination " + traveler.getLocationName() + " discovered!");
				String name = traveler.getName();
				if (name == null)
					name = "[Traveler]";
				event.getPlayer().openInventory(Bukkit.createInventory(null, InventoryType.WORKBENCH, name));
				ActionListener.this.travelerService.buildGui(data);
			}
		});

	}

	@EventHandler
	public void onInventoryCloseEvent(InventoryCloseEvent event) {
		AdventurerData data = this.adventureService.getAdventurerData(event.getPlayer().getUniqueId());
		if (data == null)
			return;
		data.resetRecipes();
	}

	@EventHandler
	public void onTravelerLoad(ChunkLoadEvent event) {
		Long id;
		Traveler traveler;
		for (Entity entity : event.getChunk().getEntities()) {
			if (!(entity instanceof LivingEntity))
				continue;
			id = entity.getPersistentDataContainer().get(TRAVELER_ID_KEY, PersistentDataType.LONG);
			if (id == null)
				continue;
			traveler = this.travelerService.loadTraveler(id);
			if (traveler == null)
				entity.remove();
			else
				traveler.setEntity((LivingEntity) entity);
		}
	}

	@EventHandler
	public void onTravelerUnload(ChunkUnloadEvent event) {
		Long id;
		Traveler traveler;
		for (Entity entity : event.getChunk().getEntities()) {
			if (!(entity instanceof LivingEntity))
				continue;
			id = entity.getPersistentDataContainer().get(TRAVELER_ID_KEY, PersistentDataType.LONG);
			if (id == null)
				continue;
			traveler = this.travelerService.loadTraveler(id);
			if (traveler == null)
				entity.remove();
			else
				traveler.setEntity(null);
		}
	}

	@EventHandler
	public void onTravelerDeath(EntityDeathEvent event) {
		Long id = event.getEntity().getPersistentDataContainer().get(TRAVELER_ID_KEY, PersistentDataType.LONG);
		if (id == null)
			return;
		Traveler traveler = this.travelerService.loadTraveler(id);
		traveler.setEntity(null);
		this.travelerService.deleteTraveler(traveler);
	}
}