package net.runeduniverse.mc.plugins.traveler.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.runeduniverse.mc.plugins.snowflake.api.Snowflake;
import net.runeduniverse.mc.plugins.snowflake.api.data.model.Location;
import net.runeduniverse.mc.plugins.snowflake.api.services.IService;
import net.runeduniverse.mc.plugins.snowflake.api.services.IStorageService;
import net.runeduniverse.mc.plugins.snowflake.api.services.modules.INeo4jModule;
import net.runeduniverse.mc.plugins.traveler.TravelerMain;
import net.runeduniverse.mc.plugins.traveler.data.AdventurerData;
import net.runeduniverse.mc.plugins.traveler.data.NamespacedKeys;
import net.runeduniverse.mc.plugins.traveler.data.model.Traveler;

public class TravelerService implements IService {

	public static TravelerService INSTANCE;

	private static final ItemStack TOKEN = new ItemStack(Material.FILLED_MAP);

	static {
		ItemMeta meta = TOKEN.getItemMeta();
		meta.setDisplayName("TRAVELER TOKEN");
		meta.setLore(Arrays.asList("To travel, open your RECIPE BOOK", "and search for the desired location!",
				"Clicking will send you there!"));
		TOKEN.setItemMeta(meta);
	}

	@Getter
	private Snowflake snowflake;
	private TravelerMain main;

	private IStorageService storageService;
	private AdventureService adventureService;

	private INeo4jModule neo4jModule;
	private Map<NamespacedKey, Traveler> keyedTraveler = new HashMap<>();

	public TravelerService(Snowflake snowflake, TravelerMain main) {
		this.snowflake = snowflake;
		this.main = main;
		INSTANCE = this;
	}

	@Override
	public void prepare() {
		this.storageService = this.snowflake.getStorageService();
		this.adventureService = this.main.getAdventureService();
		this.snowflake.getRecipeService().registerItemStack(NamespacedKeys.TOKEN_KEY, TOKEN);
	}

	public void inject(INeo4jModule module) {
		this.neo4jModule = module;
	}

	public Traveler createTraveler() {
		Traveler traveler = new Traveler();
		this.neo4jModule.getSession().save(traveler);
		return traveler;
	}

	public Traveler createTraveler(Location location) throws NullPointerException {
		World world = location.getWorld().getWorld();
		if (world == null)
			throw new NullPointerException("requested World not loaded!");
		Traveler traveler = new Traveler();
		System.out.println("loc: " + location.toString());
		traveler.setHome(location.copyTo(new Location()));
		traveler.setLocation(location.copyTo(new Location()));
		this.saveTraveler(traveler);
		traveler.summonEntity();
		this.registerTraveler(traveler);
		return traveler;
	}

	public Traveler createTraveler(org.bukkit.Location location) throws NullPointerException {
		Location loc = this.storageService.convert(location);
		return this.createTraveler(loc);
	}

	public Traveler loadTraveler(Long id) {
		return this.neo4jModule.getSession().load(Traveler.class, id, 4);
	}

	public void saveTraveler(Traveler traveler) {
		this.neo4jModule.getSession().save(traveler, 2);
	}

	public void registerTraveler(Traveler traveler) {
		if (this.keyedTraveler.containsValue(traveler))
			return;
		NamespacedKey key = traveler.getNamespacedKey();
		Bukkit.addRecipe(genMapRecipe(key, traveler));
		this.keyedTraveler.put(key, traveler);
	}

	public void removeTraveler(Traveler traveler) {
		this.keyedTraveler.remove(traveler.getNamespacedKey());
		this.neo4jModule.getSession().unload(traveler.getHome());
		this.neo4jModule.getSession().unload(traveler.getLocation());
	}

	public void deleteTraveler(Traveler traveler) {
		this.keyedTraveler.remove(traveler.getNamespacedKey());
		this.neo4jModule.getSession().delete(traveler);
		this.neo4jModule.getSession().delete(traveler.getHome());
		this.neo4jModule.getSession().delete(traveler.getLocation());
		this.adventureService.removeRecordsOf(traveler);
	}

	public void buildGui(AdventurerData data) {
		List<NamespacedKey> fakeRecipes = new ArrayList<>();
		for (Traveler t : data.getAdventurer().getTravelers()) {
			if (this.keyedTraveler.containsValue(t))
				fakeRecipes.add(t.getNamespacedKey());
		}
		data.showAltRecipes(fakeRecipes);

		// TODO rm debug stuff

		data.getPlayer().discoverRecipes(fakeRecipes);

		for (NamespacedKey namespacedKey : fakeRecipes) {
			System.out.println("show: " + namespacedKey);
		}
	}

	public void teleport(Player player, NamespacedKey travelerKey) {
		this.main.getLogger().info(
				"trying to teleport Player<" + player.getName() + "> to Traveler with key alias <" + travelerKey + ">");
		Traveler traveler = this.keyedTraveler.get(travelerKey);
		if (traveler == null)
			player.sendMessage("The requested Traveler cant be found!");
		this.snowflake.getPlayerService().getData(player).teleport(traveler.getLocation());
	}

	public Info getInfo(Traveler traveler) {
		return new Info(traveler);
	}

	@SuppressWarnings("deprecation")
	private static ShapelessRecipe genMapRecipe(NamespacedKey key, Traveler traveler) {
		ItemStack stack = new ItemStack(Material.FILLED_MAP);
		ItemMeta meta = stack.getItemMeta();
		meta.setDisplayName(traveler.getName());
		stack.setItemMeta(meta);
		ShapelessRecipe recipe = new ShapelessRecipe(key, stack);
		recipe.addIngredient(new RecipeChoice.ExactChoice(TOKEN));
		return recipe;
	}

	@RequiredArgsConstructor
	public class Info {
		private final Traveler traveler;

		public String full() {
			return String.join("\n│ ", "┌─── Traveler Info ───", "ID: " + this.traveler.getId(), name(), invulnerable(),
					home(), destname(), visibility(), movement(), destination(), owner());
		}

		public String name() {
			return "Name:             " + this.traveler.getName();
		}

		public String destname() {
			return "Destination Name: " + this.traveler.getLocationName();
		}

		public String visibility() {
			return "Visibility:         ?";
		}

		public String movement() {
			return "Movement:       " + (this.traveler.canMove() ? "yes" : "no");
		}

		public String invulnerable() {
			return "Invulnerable:   " + (this.traveler.isInvulnerable() ? "yes" : "no");
		}

		public String home() {
			return "Home:             " + this.traveler.getHome();
		}

		public String destination() {
			return "Destination:     " + this.traveler.getLocation();
		}

		public String owner() {
			return "Owner:           ?";
		}
	}
}
