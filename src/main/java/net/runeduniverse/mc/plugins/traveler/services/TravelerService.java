package net.runeduniverse.mc.plugins.traveler.services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import lombok.Getter;
import net.runeduniverse.mc.plugins.snowflake.api.Snowflake;
import net.runeduniverse.mc.plugins.snowflake.api.services.IService;
import net.runeduniverse.mc.plugins.snowflake.api.services.modules.INeo4jModule;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.data.AdventurerData;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;

public class TravelerService implements IService {

	public static TravelerService INSTANCE;

	@Getter
	private Snowflake snowflake;
	private TravelerMain main;

	private INeo4jModule neo4jModule;
	private Map<Traveler, NamespacedKey> loadedTraveler = new HashMap<>();

	public TravelerService(Snowflake snowflake, TravelerMain main) {
		this.snowflake = snowflake;
		this.main = main;
		INSTANCE = this;
	}

	@Override
	public void prepare() {
	}

	public Traveler loadTraveler(Long id) {
		Traveler traveler = this.neo4jModule.getSession().load(Traveler.class, id);
		if (traveler == null)
			return null;
		this.registerTraveler(traveler);
		return traveler;
	}

	public void registerTraveler(Traveler traveler) {
		if (this.loadedTraveler.containsKey(traveler))
			return;
		NamespacedKey key = new NamespacedKey(this.main, "loc:" + traveler.getId());
		Bukkit.addRecipe(new ShapelessRecipe(key, genMap(traveler.getName())));
		this.loadedTraveler.put(traveler, key);
	}

	public void removeTraveler(Traveler traveler) {
		this.loadedTraveler.remove(traveler);
	}

	private static ItemStack genMap(String name) {
		ItemStack stack = new ItemStack(Material.FILLED_MAP);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(name);
		stack.setItemMeta(meta);
		return stack;
	}

	public void buildGui(AdventurerData data) {
		List<NamespacedKey> fakeRecipes = new ArrayList<>();
		for (Traveler t : data.getAdventurer().getTravelers()) {
			NamespacedKey key = this.loadedTraveler.get(t);
			if (key != null)
				fakeRecipes.add(key);
		}
		data.showAltRecipes(fakeRecipes);
	}
}
