package net.runeduniverse.mc.plugins.traveler;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

import net.md_5.bungee.api.ChatColor;
import net.runeduniverse.mc.plugins.traveler.model.LocationToken;

public class PlayerMap {
	
	public static PlayerMap load(UUID uuid) {
		File optionsFile = new File("traveler-data"+System.getProperty("file.separator")+uuid.toString()+".json");
		PlayerMap map = null;
		
		if(!optionsFile.exists()) {
			map = new PlayerMap();
			map.uuid = uuid;
		}		
		else
			try {
				map = new Gson().fromJson(new FileReader(optionsFile), PlayerMap.class);
			} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
				e.printStackTrace();
				return null;
			}
		
		return map;
	}
	
	public Boolean save() {
		
		new File("traveler-data").mkdirs();
		
			try {
				FileWriter writer = new FileWriter("traveler-data"+System.getProperty("file.separator")+this.uuid.toString()+".json");
				new Gson().toJson(this, writer);
				writer.close();
			} catch (JsonIOException | IOException e) {
				e.printStackTrace();
				return false;
			}
		
		return true;
	}
	
	// variables
	private UUID uuid;
	private List<Long> visitedPlaces = new ArrayList<>();
	
	// set/get
	public Boolean addPlace(Long id) {
		if(this.visitedPlaces.contains(id))
			return false;
		
		this.visitedPlaces.add(id);
		return true;
	}
	public List<Long> getVisitedPlaces() {
		return this.visitedPlaces;
	}

	public Inventory getInv(String name) {
		
		NamespacedKey id = new NamespacedKey(Main.getPlugin(), "id");
		
		Inventory inv = Bukkit.createInventory(null, 45, name);
		inv.clear();
		
		for (Long vpid : visitedPlaces) {
			LocationToken entry = Options.getOptions().getMapEntry(vpid);
			if(entry == null) {
				visitedPlaces.remove(vpid);
				continue;
			}
			
			ItemStack stack = new ItemStack(Material.FILLED_MAP);
			ItemMeta meta = stack.getItemMeta();
			
			meta.setDisplayName(entry.getName());
			List<String> lore = new ArrayList<String>();
			
			lore.add(ChatColor.GRAY+entry.getWorld());
			lore.add(ChatColor.GRAY+entry.getX().toString()+' '+entry.getY().toString()+' '+entry.getZ().toString());
			meta.setLore(lore);
			
			meta.getPersistentDataContainer().set(id, PersistentDataType.LONG, entry.getID());
			stack.setItemMeta(meta);
			inv.addItem(stack);
		}
		
		return inv;
	}
	
}
